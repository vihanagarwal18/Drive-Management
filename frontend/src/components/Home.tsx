import React, { useState } from 'react';
import { Link, Outlet } from 'react-router-dom';
import Loading from './Loading';
import './Home.css';

interface HomeProps {
  username: string;
  onLogout: () => void;
  onDeleteAccount: () => void;
  isDarkMode: boolean;
}

const Home: React.FC<HomeProps> = ({ username, onLogout, onDeleteAccount, isDarkMode }) => {
  const [loading, setLoading] = useState(false);

  const handleLogout = () => {
    setLoading(true);
    onLogout();
  };

  const handleDeleteAccount = () => {
    setLoading(true);
    onDeleteAccount();
  };

  return (
    <div className={`home-container ${isDarkMode ? 'dark-mode' : ''}`}>
      {loading && <Loading />}
      <nav className="sidebar">
        <div className="sidebar-header">
          <h3>Lenk</h3>
        </div>
        <div className="user-profile">
          <i className="fas fa-user-circle"></i>
          <span>Hello, {username}</span>
        </div>
        <ul>
          <li>
            <Link to="/">
              <i className="fas fa-home"></i> HOME
            </Link>
          </li>
          <li>
            <Link to="/settings">
              <i className="fas fa-cog"></i> SETTING
            </Link>
          </li>
        </ul>
        <div className="sidebar-footer">
          <ul>
            <li>
              <a href="#" onClick={handleLogout}>
                <i className="fas fa-sign-out-alt"></i> LOGOUT
              </a>
            </li>
            <li>
              <a href="#" onClick={handleDeleteAccount}>
                <i className="fas fa-trash"></i> DELETE ACCOUNT
              </a>
            </li>
          </ul>
        </div>
      </nav>
      <main className="main-content">
        <header>
        </header>
        <section className="content">
          <Outlet />
        </section>
      </main>
    </div>
  );
};

export default Home;