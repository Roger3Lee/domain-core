/**
 * domain-core frontend utilities
 *
 * Mirrors the backend BaseDomain / LoadFlag concept:
 *  - Every domain object carries a `changed` flag (default false).
 *  - Editing any field via `setField` marks `changed = true` automatically.
 *  - `LoadFlag` controls which related data the backend should load/save.
 */

/**
 * Initialize a domain object received from the server.
 * Ensures all nested sub-domains also have `changed=false`.
 *
 * @param {object} obj - plain domain data from server
 * @returns {object}
 */
export function initDomain(obj) {
  if (!obj) return obj;
  const result = { ...obj, changed: obj.changed ?? false };

  // Initialize list sub-domains
  Object.keys(result).forEach((key) => {
    if (Array.isArray(result[key])) {
      result[key] = result[key].map((item) =>
        typeof item === 'object' && item !== null
          ? { ...item, changed: item.changed ?? false }
          : item
      );
    } else if (
      typeof result[key] === 'object' &&
      result[key] !== null &&
      key !== 'loadFlag'
    ) {
      result[key] = { ...result[key], changed: result[key].changed ?? false };
    }
  });

  return result;
}

/**
 * Produce a new domain object with the specified field updated
 * and `changed` set to true.
 *
 * @param {object} domain  - current domain object
 * @param {string} field   - field name
 * @param {*}      value   - new value
 * @returns {object}       - new domain object with changed=true
 */
export function setField(domain, field, value) {
  return { ...domain, [field]: value, changed: true };
}

/**
 * Produce a new list where the item at `index` has `field` updated
 * and its `changed` flag set to true.
 *
 * @param {Array}  list   - current list
 * @param {number} index  - item index
 * @param {string} field  - field name
 * @param {*}      value  - new value
 * @returns {Array}       - new list
 */
export function setListItemField(list, index, field, value) {
  return list.map((item, i) =>
    i === index ? { ...item, [field]: value, changed: true } : item
  );
}

/**
 * Add a new item to a list. The new item gets changed=true so the
 * backend treats it as an insert.
 *
 * @param {Array}  list    - current list
 * @param {object} newItem - new item (id should be null/undefined for insert)
 * @returns {Array}
 */
export function addListItem(list, newItem) {
  return [...(list ?? []), { ...newItem, changed: true }];
}

/**
 * Remove an item from a list by index.
 *
 * @param {Array}  list  - current list
 * @param {number} index - item index to remove
 * @returns {Array}
 */
export function removeListItem(list, index) {
  return list.filter((_, i) => i !== index);
}

/**
 * Build a LoadFlag object for Family domain.
 * Controls which related entities the backend loads.
 *
 * @param {object} flags - e.g. { loadFamilyAddressDomain: true }
 * @returns {object}
 */
export function buildLoadFlag(flags = {}) {
  return {
    loadAll: false,
    loadFamilyAddressDomain: false,
    loadFamilyMemberDomain: false,
    ...flags,
  };
}
