import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Login.css';

const ForgotPassword = () => {
  const [username, setUsername] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleForgotPassword = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setMessage('');
    try {
      const response = await fetch(`/internal/v1/auth/forgot-password/${username}`, {
        method: 'POST',
      });

      if (response.status === 404) {
        const errorText = await response.text();
        setError(errorText || 'User not found');
      } else if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to send password reset email');
      } else {
        const email = await response.text();
        setMessage(email);
      }
    } catch (err: any) {
      setError(err.message || 'Failed to send password reset email. Please try again.');
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h2>Forgot Password</h2>
        <p>Enter your username to receive your password.</p>
        <form onSubmit={handleForgotPassword}>
          <input
            type="text"
            placeholder="Enter your username..."
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <button type="submit">Submit</button>
        </form>
        {message && <p className="success">{message}</p>}
        {error && <p className="error">{error}</p>}
        <p>
          <Link to="/login">Login</Link> | <Link to="/register">Register</Link>
        </p>
      </div>
    </div>
  );
};

export default ForgotPassword;