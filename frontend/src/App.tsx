import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Home from './components/Home';
import Register from './components/Register';
import ForgotPassword from './components/ForgotPassword';
import Loading from './components/Loading';
import './App.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userId, setUserId] = useState('');
  const [loading, setLoading] = useState(true);

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
      } finally {
        setLoading(false);
      }
    };
    validateToken();
  }, []);

  const handleLogin = (userId: string) => {
    setIsAuthenticated(true);
    setUserId(userId);
  };

  const handleRegister = (userId: string) => {
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
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteAccount = async () => {
    try {
      await fetch(`/internal/v1/auth/delete/${userId}`, { method: 'DELETE' });
      setIsAuthenticated(false);
      setUserId('');
    } catch (error) {
      console.error('Account deletion failed', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Router>
      <div className="App">
        {loading && <Loading />}
        <Routes>
          <Route
            path="/"
            element={
              isAuthenticated ? (
                <Home onLogout={handleLogout} onDeleteAccount={handleDeleteAccount} />
              ) : (
                <Navigate to="/login" />
              )
            }
          />
          <Route
            path="/login"
            element={
              isAuthenticated ? <Navigate to="/" /> : <Login onLogin={handleLogin} />
            }
          />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route
            path="/register"
            element={
              isAuthenticated ? (
                <Navigate to="/" />
              ) : (
                <Register onRegister={handleRegister} />
              )
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
