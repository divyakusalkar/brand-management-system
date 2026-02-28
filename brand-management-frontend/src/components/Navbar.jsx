import { Link, useLocation } from 'react-router-dom';

export default function Navbar() {
  const { pathname } = useLocation();

  const navLink = (to, label) => (
    <Link
      to={to}
      className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${
        pathname === to
          ? 'bg-blue-700 text-white'
          : 'text-blue-100 hover:bg-blue-700 hover:text-white'
      }`}
    >
      {label}
    </Link>
  );

  return (
    <nav className="bg-blue-800 shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <span className="text-white text-xl font-bold tracking-wide">
            Brand<span className="text-blue-300">Hub</span>
          </span>
        </div>
        <div className="flex items-center gap-2">
          {navLink('/dashboard', 'Dashboard')}
          {navLink('/brands/add', 'Add Brand')}
        </div>
      </div>
    </nav>
  );
}
