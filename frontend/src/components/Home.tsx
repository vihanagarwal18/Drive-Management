import React from 'react';
import './Home.css';

interface HomeProps {
  onLogout: () => void;
  onDeleteAccount: () => void;
}

const Home: React.FC<HomeProps> = ({ onLogout, onDeleteAccount }) => {
  return (
    <div className="home-container">
      <nav className="sidebar">
        <div className="sidebar-header">
          <h3>Lenk</h3>
        </div>
        <ul>
          <li>
            <a href="#">
              <i className="fas fa-home"></i> HOME
            </a>
          </li>
          <li>
            <a href="#">
              <i className="fas fa-cog"></i> SETTING
            </a>
          </li>
        </ul>
        <div className="sidebar-footer">
          <ul>
            <li>
              <a href="#" onClick={onLogout}>
                <i className="fas fa-sign-out-alt"></i> LOGOUT
              </a>
            </li>
            <li>
              <a href="#" onClick={onDeleteAccount}>
                <i className="fas fa-trash"></i> DELETE ACCOUNT
              </a>
            </li>
          </ul>
        </div>
      </nav>
      <main className="main-content">
        <header>
          <input type="search" placeholder="Search..." />
        </header>
        <section className="content">
          {/* Add your main content here */}
        </section>
      </main>
    </div>
  );
};

export default Home;