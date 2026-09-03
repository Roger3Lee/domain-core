/**
 * Reference implementation: change tracking for frontend domain models.
 *
 * Deep-clones API response data as a clean snapshot, then recursively marks
 * changed=true for edited objects that expose a changed field.
 */

import type { Dispatch, SetStateAction } from 'react';
import { useCallback, useEffect, useRef, useState } from 'react';

const SNAPSHOT_KEY = Symbol.for('__domainSnapshot__');

/** Attach a non-enumerable clean snapshot to successful API response data. */
export function attachSnapshot(data: any): any {
  if (data && typeof data === 'object') {
    try {
      const snapshot = structuredClone(data);
      Object.defineProperty(data, SNAPSHOT_KEY, {
        value: snapshot,
        enumerable: false,
        writable: false,
        configurable: true,
      });
    } catch {
      // Skip data that cannot be structured-cloned.
    }
  }
  return data;
}

export function getSnapshot<T>(data: T): T | undefined {
  if (data && typeof data === 'object') {
    return (data as any)[SNAPSHOT_KEY] as T | undefined;
  }
  return undefined;
}

function isDeepEqual(a: any, b: any, skipChanged = true): boolean {
  if (a === b) return true;
  if (!a || !b) return false;
  if (typeof a !== 'object' || typeof b !== 'object') return false;

  const keysA = Object.keys(a);
  const keysB = Object.keys(b);
  if (keysA.length !== keysB.length) return false;

  for (const key of keysA) {
    if (skipChanged && key === 'changed') continue;
    if (!keysB.includes(key) || !isDeepEqual(a[key], b[key], skipChanged)) {
      return false;
    }
  }
  return true;
}

function markDeepChanged<T>(prev: any, next: T): T {
  if (!next || typeof next !== 'object') return next;

  if (Array.isArray(next)) {
    return next.map((item, index) =>
      markDeepChanged((prev as any)?.[index], item),
    ) as T;
  }

  const newObj: any = { ...next };
  if ('changed' in newObj && !isDeepEqual(prev, next)) {
    newObj.changed = true;
  }

  for (const key of Object.keys(newObj)) {
    if (key === 'changed') continue;
    const value = newObj[key];
    if (value && typeof value === 'object') {
      newObj[key] = markDeepChanged(prev?.[key], value);
    }
  }
  return newObj as T;
}

/**
 * React state hook for domain models that automatically updates changed tags.
 * Returns [data, setData, resetSnapshot].
 */
export function useAutoMarkChanged<T>(
  initialData: T,
): [T, Dispatch<SetStateAction<T>>, (cleanData: T) => void] {
  const [data, setDataRaw] = useState<T>(initialData);
  const snapshotRef = useRef<T>(resolveSnapshot(initialData));
  const isMarkingRef = useRef(false);

  useEffect(() => {
    if (isMarkingRef.current) {
      isMarkingRef.current = false;
      return;
    }
    if (!isDeepEqual(snapshotRef.current, data)) {
      const marked = markDeepChanged(snapshotRef.current, data);
      if (!isDeepEqual(marked, data, false)) {
        isMarkingRef.current = true;
        setDataRaw(marked);
      }
    }
  }, [data]);

  const resetSnapshot = useCallback((cleanData: T) => {
    snapshotRef.current = resolveSnapshot(cleanData);
    isMarkingRef.current = true;
    setDataRaw(cleanData);
  }, []);

  return [data, setDataRaw, resetSnapshot];
}

function resolveSnapshot<T>(data: T): T {
  const attached = getSnapshot(data);
  if (attached) return attached;
  try {
    return structuredClone(data);
  } catch {
    return JSON.parse(JSON.stringify(data));
  }
}

/** Remove top-level frontend temporary IDs before an API submission. */
export function stripInternalFields<T extends { _tempId?: string }>(
  data: T,
): Omit<T, '_tempId'>;
export function stripInternalFields<T extends { _tempId?: string }>(
  data: T[],
): Omit<T, '_tempId'>[];
export function stripInternalFields<T extends { _tempId?: string }>(
  data: T | T[],
): Omit<T, '_tempId'> | Omit<T, '_tempId'>[] {
  if (Array.isArray(data)) {
    return data.map(({ _tempId, ...rest }) => rest as Omit<T, '_tempId'>);
  }
  const { _tempId, ...rest } = data;
  return rest as Omit<T, '_tempId'>;
}
