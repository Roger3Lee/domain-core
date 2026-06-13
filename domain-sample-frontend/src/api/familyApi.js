/**
 * Family domain API client.
 * Talks to the backend FamilyController at /family/v1
 */

const BASE = '/sample/family/v1';

/**
 * POST /family/v1/{key}
 * Find a FamilyDomain by key with optional loadFlag.
 *
 * @param {number|string} key - primary key
 * @param {object} [loadFlag] - optional load flag for related tables
 * @returns {Promise<object>} FamilyDomain
 */
export async function queryFamily(key, loadFlag) {
  const res = await fetch(`${BASE}/${key}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: loadFlag ? JSON.stringify(loadFlag) : null,
  });
  if (!res.ok) throw new Error(`Query failed: ${res.status}`);
  return res.json();
}

/**
 * PUT /family/v1
 * Insert a new FamilyDomain.
 *
 * @param {object} domain - FamilyDomain (no id)
 * @returns {Promise<number>} new id
 */
export async function insertFamily(domain) {
  const res = await fetch(BASE, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(domain),
  });
  if (!res.ok) throw new Error(`Insert failed: ${res.status}`);
  return res.json();
}

/**
 * POST /family/v1
 * Update an existing FamilyDomain.
 * Only fields with changed=true are actually persisted by the backend.
 *
 * @param {object} domain - FamilyDomain with loadFlag
 * @returns {Promise<boolean>}
 */
export async function updateFamily(domain) {
  const res = await fetch(BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(domain),
  });
  if (!res.ok) throw new Error(`Update failed: ${res.status}`);
  return res.json();
}

/**
 * DELETE /family/v1/{key}
 *
 * @param {number|string} key - primary key
 * @returns {Promise<boolean>}
 */
export async function deleteFamily(key) {
  const res = await fetch(`${BASE}/${key}`, { method: 'DELETE' });
  if (!res.ok) throw new Error(`Delete failed: ${res.status}`);
  return res.json();
}
