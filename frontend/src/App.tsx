import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Home from './components/Home';
import './App.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userId, setUserId] = useState('');

  useEffect(() => {
    const validateToken = async () => {
      try {
        const response = await fetch('/internal/v1/auth/validate');
        if (response.ok) {
          const data = await response.json();
          if (data.valid) {
            setIsAuthenticated(true);
            setUserId(data.userId);
          }
        }
      } catch (error) {
        console.error('Token validation failed', error);
      }
    };
    validateToken();
  }, []);

  const handleLogin = (userId: string) => {
    setIsAuthenticated(true);
    setUserId(userId);
  };

  const handleLogout = async () => {
    try {
      await fetch('/internal/v1/auth/logout', { method: 'POST' });
      setIsAuthenticated(false);
      setUserId('');
    } catch (error) {
      console.error('Logout failed', error);
    }
  };

  const handleDeleteAccount = async () => {
    try {
      await fetch(`/internal/v1/auth/delete/${userId}`, { method: 'DELETE' });
      setIsAuthenticated(false);
      setUserId('');
    } catch (error) {
      console.error('Account deletion failed', error);
    }
  };

  return (
    <div className="App">
      {isAuthenticated ? (
        <Home onLogout={handleLogout} onDeleteAccount={handleDeleteAccount} />
      ) : (
        <Login onLogin={handleLogin} />
      )}
    </div>
  );
}

export default App;
