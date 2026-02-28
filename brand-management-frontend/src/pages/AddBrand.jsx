import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { getAllChains, createBrand } from '../api/brandApi';

const INITIAL = { brandName: '', chainId: '' };

export default function AddBrand() {
  const navigate = useNavigate();
  const [form, setForm] = useState(INITIAL);
  const [errors, setErrors] = useState({});
  const [chains, setChains] = useState([]);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    getAllChains()
      .then((res) => setChains(res.data))
      .catch(() => toast.error('Failed to load companies'));
  }, []);

  /* ── Field change ─────────────────────────────────────────────────── */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  /* ── Validate ─────────────────────────────────────────────────────── */
  const validate = () => {
    const errs = {};
    if (!form.brandName.trim()) errs.brandName = 'Brand name is required';
    else if (form.brandName.trim().length > 50) errs.brandName = 'Brand name must not exceed 50 characters';
    if (!form.chainId) errs.chainId = 'Please select a company';
    return errs;
  };

  /* ── Submit ───────────────────────────────────────────────────────── */
  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length) { setErrors(errs); return; }

    setSubmitting(true);
    try {
      await createBrand({ brandName: form.brandName.trim(), chainId: Number(form.chainId) });
      toast.success('Brand created successfully!');
      navigate('/dashboard');
    } catch (err) {
      const msg = err.response?.data?.message || 'Failed to create brand';
      toast.error(msg);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="max-w-lg mx-auto px-4 py-10">
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-8">
        <h1 className="text-xl font-bold text-gray-900 mb-1">Add New Brand</h1>
        <p className="text-sm text-gray-500 mb-6">Fill in the details to register a new brand.</p>

        <form onSubmit={handleSubmit} className="space-y-5" noValidate>

          {/* Brand Name */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Brand Name <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              name="brandName"
              value={form.brandName}
              onChange={handleChange}
              maxLength={50}
              placeholder="e.g. Alpha Brand A"
              className={`w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 ${
                errors.brandName
                  ? 'border-red-400 focus:ring-red-300'
                  : 'border-gray-300 focus:ring-blue-500'
              }`}
            />
            {errors.brandName && (
              <p className="mt-1 text-xs text-red-600">{errors.brandName}</p>
            )}
            <p className="mt-1 text-xs text-gray-400 text-right">
              {form.brandName.length}/50
            </p>
          </div>

          {/* Company */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Company <span className="text-red-500">*</span>
            </label>
            <select
              name="chainId"
              value={form.chainId}
              onChange={handleChange}
              className={`w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 ${
                errors.chainId
                  ? 'border-red-400 focus:ring-red-300'
                  : 'border-gray-300 focus:ring-blue-500'
              }`}
            >
              <option value="">-- Select a Company --</option>
              {chains.map((c) => (
                <option key={c.chainId} value={c.chainId}>
                  {c.chainName}
                </option>
              ))}
            </select>
            {errors.chainId && (
              <p className="mt-1 text-xs text-red-600">{errors.chainId}</p>
            )}
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-2">
            <button
              type="submit"
              disabled={submitting}
              className="flex-1 py-2 bg-blue-700 text-white text-sm font-medium rounded-lg hover:bg-blue-800 transition-colors disabled:opacity-60 disabled:cursor-not-allowed"
            >
              {submitting ? 'Creating…' : 'Create Brand'}
            </button>
            <button
              type="button"
              onClick={() => navigate('/dashboard')}
              className="flex-1 py-2 border border-gray-300 text-gray-700 text-sm font-medium rounded-lg hover:bg-gray-50 transition-colors"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
