import { useState } from 'react';
import { useDomain } from '../domain-core/useDomain';
import { queryFamily, insertFamily, updateFamily, deleteFamily } from '../api/familyApi';
import './FamilyPage.css';

/**
 * FamilyPage - demonstrates the domain-core frontend pattern:
 *
 * 1. LoadFlag checkboxes control which relations are loaded from the backend.
 * 2. Any field edit automatically sets `changed = true` on that object.
 * 3. On submit, only objects with `changed = true` are actually persisted.
 * 4. The request payload is printed as JSON so you can inspect the changed flags.
 */
export default function FamilyPage() {
  const [searchKey, setSearchKey] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);
  const [lastPayload, setLastPayload] = useState(null);

  const {
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
  } = useDomain(null, {
    loadFamilyAddressDomain: true,
    loadFamilyMemberDomain: true,
  });

  // ── Query ────────────────────────────────────────────────────────────────
  const handleQuery = async () => {
    if (!searchKey) { setMessage({ type: 'error', text: 'Please enter a key' }); return; }
    setLoading(true);
    setMessage(null);
    try {
      const payload = buildFindPayload(Number(searchKey));
      setLastPayload(payload);
      const data = await queryFamily(payload);
      resetDomain(data);
      setMessage({ type: 'success', text: 'Loaded successfully' });
    } catch (e) {
      setMessage({ type: 'error', text: e.message });
    } finally {
      setLoading(false);
    }
  };

  // ── Insert ───────────────────────────────────────────────────────────────
  const handleInsert = async () => {
    if (!domain) { setMessage({ type: 'error', text: 'No data to insert' }); return; }
    setLoading(true);
    setMessage(null);
    try {
      const payload = { ...domain, loadFlag };
      setLastPayload(payload);
      const newId = await insertFamily(payload);
      updateField('id', newId);
      setMessage({ type: 'success', text: `Inserted, new id = ${newId}` });
    } catch (e) {
      setMessage({ type: 'error', text: e.message });
    } finally {
      setLoading(false);
    }
  };

  // ── Update ───────────────────────────────────────────────────────────────
  const handleUpdate = async () => {
    if (!domain?.id) { setMessage({ type: 'error', text: 'No domain loaded' }); return; }
    setLoading(true);
    setMessage(null);
    try {
      const payload = buildUpdatePayload();
      setLastPayload(payload);
      const ok = await updateFamily(payload);
      setMessage({ type: 'success', text: ok ? 'Updated successfully' : 'Update returned false' });
    } catch (e) {
      setMessage({ type: 'error', text: e.message });
    } finally {
      setLoading(false);
    }
  };

  // ── Delete ───────────────────────────────────────────────────────────────
  const handleDelete = async () => {
    if (!domain?.id) { setMessage({ type: 'error', text: 'No domain loaded' }); return; }
    if (!window.confirm(`Delete family id=${domain.id}?`)) return;
    setLoading(true);
    setMessage(null);
    try {
      setLastPayload({ key: domain.id });
      const ok = await deleteFamily(domain.id);
      if (ok) { resetDomain(null); setMessage({ type: 'success', text: 'Deleted' }); }
      else setMessage({ type: 'error', text: 'Delete returned false' });
    } catch (e) {
      setMessage({ type: 'error', text: e.message });
    } finally {
      setLoading(false);
    }
  };

  const handleNewDomain = () => {
    resetDomain({ name: '', personCount: 0, familyAddress: null, familyMemberList: [] });
    setMessage(null);
  };

  // ── Render ───────────────────────────────────────────────────────────────
  return (
    <div className="fp-root">
      <h2 className="fp-title">Family Domain Editor</h2>

      {/* ── LoadFlag controls ── */}
      <section className="fp-section">
        <h3>LoadFlag <span className="fp-hint">(controls which relations are loaded / saved)</span></h3>
        <div className="fp-row">
          <label>
            <input
              type="checkbox"
              checked={loadFlag.loadFamilyAddressDomain}
              onChange={(e) => setLoadFlagField('loadFamilyAddressDomain', e.target.checked)}
            />
            {' '}Load Address
          </label>
          <label>
            <input
              type="checkbox"
              checked={loadFlag.loadFamilyMemberDomain}
              onChange={(e) => setLoadFlagField('loadFamilyMemberDomain', e.target.checked)}
            />
            {' '}Load Members
          </label>
          <label>
            <input
              type="checkbox"
              checked={loadFlag.loadAll}
              onChange={(e) => setLoadFlagField('loadAll', e.target.checked)}
            />
            {' '}Load All
          </label>
        </div>
      </section>

      {/* ── Query bar ── */}
      <section className="fp-section fp-row">
        <input
          className="fp-input"
          placeholder="Family ID"
          value={searchKey}
          onChange={(e) => setSearchKey(e.target.value)}
        />
        <button className="fp-btn" onClick={handleQuery} disabled={loading}>Query</button>
        <button className="fp-btn fp-btn--secondary" onClick={handleNewDomain} disabled={loading}>+ New</button>
      </section>

      {message && (
        <div className={`fp-msg fp-msg--${message.type}`}>{message.text}</div>
      )}

      {/* ── Main domain form ── */}
      {domain && (
        <>
          <section className="fp-section fp-card">
            <div className="fp-card-header">
              <span>Family
                {domain.changed && <span className="fp-badge fp-badge--changed">changed</span>}
              </span>
              <span className="fp-id">id: {domain.id ?? '(new)'}</span>
            </div>
            <div className="fp-fields">
              <label>
                Name
                <input
                  className="fp-input"
                  value={domain.name ?? ''}
                  onChange={(e) => updateField('name', e.target.value)}
                />
              </label>
              <label>
                Person Count
                <input
                  className="fp-input"
                  type="number"
                  value={domain.personCount ?? ''}
                  onChange={(e) => updateField('personCount', Number(e.target.value))}
                />
              </label>
            </div>
          </section>

          {/* ── FamilyAddress sub-domain ── */}
          {loadFlag.loadFamilyAddressDomain || loadFlag.loadAll ? (
            <section className="fp-section fp-card fp-card--sub">
              <div className="fp-card-header">
                <span>Address
                  {domain.familyAddress?.changed && (
                    <span className="fp-badge fp-badge--changed">changed</span>
                  )}
                  {!domain.familyAddress && (
                    <button
                      className="fp-btn fp-btn--sm"
                      onClick={() => updateSubDomainField('familyAddress', '_init', true)}
                    >+ Add Address</button>
                  )}
                </span>
              </div>
              {domain.familyAddress && (
                <div className="fp-fields">
                  <label>
                    Address Name
                    <input
                      className="fp-input"
                      value={domain.familyAddress.addressName ?? ''}
                      onChange={(e) => updateSubDomainField('familyAddress', 'addressName', e.target.value)}
                    />
                  </label>
                  <label>
                    Family Name (ref)
                    <input
                      className="fp-input"
                      value={domain.familyAddress.familyName ?? ''}
                      onChange={(e) => updateSubDomainField('familyAddress', 'familyName', e.target.value)}
                    />
                  </label>
                </div>
              )}
            </section>
          ) : null}

          {/* ── FamilyMember list ── */}
          {loadFlag.loadFamilyMemberDomain || loadFlag.loadAll ? (
            <section className="fp-section fp-card fp-card--sub">
              <div className="fp-card-header">
                <span>Members ({(domain.familyMemberList ?? []).length})</span>
                <button
                  className="fp-btn fp-btn--sm"
                  onClick={() => addItem('familyMemberList', { name: '', phone: '', type: '' })}
                >+ Add Member</button>
              </div>
              {(domain.familyMemberList ?? []).map((member, idx) => (
                <div key={idx} className={`fp-list-item${member.changed ? ' fp-list-item--changed' : ''}`}>
                  <span className="fp-list-idx">#{idx + 1}</span>
                  {member.changed && <span className="fp-badge fp-badge--changed">changed</span>}
                  <div className="fp-fields">
                    <label>
                      Name
                      <input
                        className="fp-input"
                        value={member.name ?? ''}
                        onChange={(e) => updateListItemField('familyMemberList', idx, 'name', e.target.value)}
                      />
                    </label>
                    <label>
                      Phone
                      <input
                        className="fp-input"
                        value={member.phone ?? ''}
                        onChange={(e) => updateListItemField('familyMemberList', idx, 'phone', e.target.value)}
                      />
                    </label>
                    <label>
                      Type
                      <input
                        className="fp-input"
                        value={member.type ?? ''}
                        onChange={(e) => updateListItemField('familyMemberList', idx, 'type', e.target.value)}
                      />
                    </label>
                  </div>
                  <button
                    className="fp-btn fp-btn--danger fp-btn--sm"
                    onClick={() => removeItem('familyMemberList', idx)}
                  >Remove</button>
                </div>
              ))}
            </section>
          ) : null}

          {/* ── Action buttons ── */}
          <section className="fp-section fp-row">
            {!domain.id
              ? <button className="fp-btn fp-btn--primary" onClick={handleInsert} disabled={loading}>Insert</button>
              : <button className="fp-btn fp-btn--primary" onClick={handleUpdate} disabled={loading}>Save Changes</button>
            }
            {domain.id && (
              <button className="fp-btn fp-btn--danger" onClick={handleDelete} disabled={loading}>Delete</button>
            )}
          </section>
        </>
      )}

      {/* ── Payload inspector ── */}
      {lastPayload && (
        <section className="fp-section fp-inspector">
          <h3>Last Request Payload <span className="fp-hint">(notice changed flags)</span></h3>
          <pre className="fp-pre">{JSON.stringify(lastPayload, null, 2)}</pre>
        </section>
      )}
    </div>
  );
}
