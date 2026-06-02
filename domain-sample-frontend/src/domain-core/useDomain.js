import { useState, useCallback } from 'react';
import { initDomain, setField, setListItemField, addListItem, removeListItem } from './domainUtils';

/**
 * useDomain - React hook that mirrors the backend domain-core pattern.
 *
 * Features:
 *  - Tracks `changed` per domain object and per list item automatically
 *  - Exposes loadFlag state for controlling which relations are loaded/saved
 *  - Provides helpers: updateField, updateListItem, addItem, removeItem
 *
 * @param {object|null} initial - initial domain data (from API or null)
 * @param {object}      initialLoadFlag - initial load flag settings
 * @returns {object} hook API
 */
export function useDomain(initial = null, initialLoadFlag = {}) {
  const [domain, setDomain] = useState(() => initDomain(initial));
  const [loadFlag, setLoadFlag] = useState({
    loadAll: false,
    loadFamilyAddressDomain: false,
    loadFamilyMemberDomain: false,
    ...initialLoadFlag,
  });

  /** Replace the entire domain (e.g. after a fetch) */
  const resetDomain = useCallback((data) => {
    setDomain(initDomain(data));
  }, []);

  /**
   * Update a field on the root domain.
   * Automatically marks `changed = true`.
   *
   * @param {string} field
   * @param {*} value
   */
  const updateField = useCallback((field, value) => {
    setDomain((prev) => setField(prev, field, value));
  }, []);

  /**
   * Update a field on the single sub-domain (e.g. familyAddress).
   * Automatically marks sub-domain `changed = true`.
   *
   * @param {string} subKey  - key of the sub-domain on root (e.g. 'familyAddress')
   * @param {string} field   - field to update
   * @param {*}      value
   */
  const updateSubDomainField = useCallback((subKey, field, value) => {
    setDomain((prev) => ({
      ...prev,
      [subKey]: setField(prev[subKey] ?? {}, field, value),
    }));
  }, []);

  /**
   * Update a field on a list item.
   * Automatically marks the item `changed = true`.
   *
   * @param {string} listKey - key of the list on root (e.g. 'familyMemberList')
   * @param {number} index
   * @param {string} field
   * @param {*}      value
   */
  const updateListItemField = useCallback((listKey, index, field, value) => {
    setDomain((prev) => ({
      ...prev,
      [listKey]: setListItemField(prev[listKey] ?? [], index, field, value),
    }));
  }, []);

  /**
   * Add a new item to a list.
   * New item is marked `changed = true` (backend treats as insert).
   *
   * @param {string} listKey
   * @param {object} newItem - initial field values (id should be omitted)
   */
  const addItem = useCallback((listKey, newItem = {}) => {
    setDomain((prev) => ({
      ...prev,
      [listKey]: addListItem(prev[listKey], newItem),
    }));
  }, []);

  /**
   * Remove an item from a list by index.
   *
   * @param {string} listKey
   * @param {number} index
   */
  const removeItem = useCallback((listKey, index) => {
    setDomain((prev) => ({
      ...prev,
      [listKey]: removeListItem(prev[listKey] ?? [], index),
    }));
  }, []);

  /**
   * Toggle a loadFlag boolean.
   *
   * @param {string} flagKey - e.g. 'loadFamilyAddressDomain'
   * @param {boolean} value
   */
  const setLoadFlagField = useCallback((flagKey, value) => {
    setLoadFlag((prev) => ({ ...prev, [flagKey]: value }));
  }, []);

  /**
   * Build the request payload for the update API.
   * Includes loadFlag so the backend knows the update scope.
   */
  const buildUpdatePayload = useCallback(() => {
    if (!domain) return null;
    return { ...domain, loadFlag };
  }, [domain, loadFlag]);

  /**
   * Build the query request payload for the find API.
   *
   * @param {*} key - primary key value
   */
  const buildFindPayload = useCallback(
    (key) => ({ key, loadFlag }),
    [loadFlag]
  );

  return {
    domain,
    loadFlag,
    resetDomain,
    updateField,
    updateSubDomainField,
    updateListItemField,
    addItem,
    removeItem,
    setLoadFlagField,
    buildUpdatePayload,
    buildFindPayload,
  };
}
