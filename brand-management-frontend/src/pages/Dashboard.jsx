import { useEffect, useReducer, useState } from 'react';
import { Link } from 'react-router-dom';
import toast from 'react-hot-toast';
import { getAllBrands, getAllChains, deleteBrand } from '../api/brandApi';

const initialFetch = { brands: [], loading: true };
function brandsReducer(state, action) {
  switch (action.type) {
    case 'FETCH_START':   return { ...state, loading: true };
    case 'FETCH_SUCCESS': return { brands: action.payload, loading: false };
    case 'FETCH_ERROR':   return { ...state, loading: false };
    default:              return state;
  }
}

export default function Dashboard() {
  const [{ brands, loading }, dispatch] = useReducer(brandsReducer, initialFetch);
  const [chains, setChains] = useState([]);
  const [selectedChain, setSelectedChain] = useState('');
  const [search, setSearch] = useState('');
  const [deletingId, setDeletingId] = useState(null);
  const [refreshKey, setRefreshKey] = useState(0);

  /* ── Fetch chains for filter dropdown ─────────────────────────────── */
  useEffect(() => {
    getAllChains()
      .then((res) => setChains(res.data))
      .catch(() => toast.error('Failed to load companies'));
  }, []);

  /* ── Fetch brands (re-runs when chain filter or refreshKey changes) ── */
  useEffect(() => {
    let cancelled = false;
    dispatch({ type: 'FETCH_START' });
    getAllBrands(selectedChain || null)
      .then((res) => { if (!cancelled) dispatch({ type: 'FETCH_SUCCESS', payload: res.data }); })
      .catch(() => {
        if (!cancelled) {
          dispatch({ type: 'FETCH_ERROR' });
          toast.error('Failed to load brands');
        }
      });
    return () => { cancelled = true; };
  }, [selectedChain, refreshKey]);

  /* ── Soft delete handler ─────────────────────────────────────────── */
  const handleDelete = (brand) => {
    if (!window.confirm(`Delete brand "${brand.brandName}"? This cannot be undone.`)) return;
    setDeletingId(brand.brandId);
    deleteBrand(brand.brandId)
      .then(() => {
        toast.success(`Brand "${brand.brandName}" deleted successfully`);
        setRefreshKey((k) => k + 1);
      })
      .catch((err) => {
        const msg = err.response?.data?.message || 'Failed to delete brand';
        toast.error(msg);
      })
      .finally(() => setDeletingId(null));
  };

  /* ── Client-side search filter ───────────────────────────────────── */
  const filtered = brands.filter((b) =>
    b.brandName.toLowerCase().includes(search.toLowerCase()) ||
    b.chainName.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Brand Dashboard</h1>
          <p className="text-sm text-gray-500 mt-1">
            Showing all active brands
          </p>
        </div>
        <Link
          to="/brands/add"
          className="inline-flex items-center gap-2 px-4 py-2 bg-blue-700 text-white text-sm font-medium rounded-lg shadow hover:bg-blue-800 transition-colors"
        >
          <span className="text-lg leading-none">+</span> Add Brand
        </Link>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-4 mb-6 flex flex-col sm:flex-row gap-3">
        <div className="flex-1">
          <label className="block text-xs font-medium text-gray-500 mb-1">Search</label>
          <input
            type="text"
            placeholder="Search brand or company..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div className="sm:w-56">
          <label className="block text-xs font-medium text-gray-500 mb-1">Filter by Company</label>
          <select
            value={selectedChain}
            onChange={(e) => setSelectedChain(e.target.value)}
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">All Companies</option>
            {chains.map((c) => (
              <option key={c.chainId} value={c.chainId}>
                {c.chainName}
              </option>
            ))}
          </select>
        </div>
        {(selectedChain || search) && (
          <div className="sm:self-end">
            <button
              onClick={() => { setSelectedChain(''); setSearch(''); }}
              className="w-full sm:w-auto px-3 py-2 text-sm text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
            >
              Clear
            </button>
          </div>
        )}
      </div>

      {/* Table */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        {loading ? (
          <div className="flex justify-center items-center h-48">
            <div className="w-8 h-8 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : filtered.length === 0 ? (
          <div className="flex flex-col items-center justify-center h-48 text-gray-400">
            <span className="text-4xl mb-2">📭</span>
            <p className="text-sm">No brands found</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200 text-sm">
              <thead className="bg-gray-50">
                <tr>
                  {['#', 'Brand Name', 'Company', 'Status', 'Created At', 'Updated At', 'Actions'].map(
                    (h) => (
                      <th
                        key={h}
                        className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider"
                      >
                        {h}
                      </th>
                    )
                  )}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {filtered.map((brand, idx) => (
                  <tr key={brand.brandId} className="hover:bg-gray-50 transition-colors">
                    <td className="px-4 py-3 text-gray-400 font-mono">{idx + 1}</td>
                    <td className="px-4 py-3 font-medium text-gray-900">{brand.brandName}</td>
                    <td className="px-4 py-3 text-gray-600">{brand.chainName}</td>
                    <td className="px-4 py-3">
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                        Active
                      </span>
                    </td>
                    <td className="px-4 py-3 text-gray-500 whitespace-nowrap">
                      {new Date(brand.createdAt).toLocaleDateString('en-GB', {
                        day: '2-digit', month: 'short', year: 'numeric',
                      })}
                    </td>
                    <td className="px-4 py-3 text-gray-500 whitespace-nowrap">
                      {new Date(brand.updatedAt).toLocaleDateString('en-GB', {
                        day: '2-digit', month: 'short', year: 'numeric',
                      })}
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-2">
                        <Link
                          to={`/brands/edit/${brand.brandId}`}
                          className="px-3 py-1 text-xs font-medium bg-blue-50 text-blue-700 border border-blue-200 rounded-md hover:bg-blue-100 transition-colors"
                        >
                          Edit
                        </Link>
                        <button
                          onClick={() => handleDelete(brand)}
                          disabled={deletingId === brand.brandId}
                          className="px-3 py-1 text-xs font-medium bg-red-50 text-red-700 border border-red-200 rounded-md hover:bg-red-100 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                          {deletingId === brand.brandId ? 'Deleting…' : 'Delete'}
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Count */}
      {!loading && (
        <p className="text-xs text-gray-400 text-right mt-3">
          {filtered.length} brand{filtered.length !== 1 ? 's' : ''} shown
        </p>
      )}
    </div>
  );
}
