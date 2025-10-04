import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Loading from './Loading';
import './Login.css';
import logo from '../logo.svg';

interface LoginProps {
  onLogin: (userId: string) => void;
}

const Login: React.FC<LoginProps> = ({ onLogin }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const response = await fetch('/internal/v1/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Login failed');
      }

      const data = await response.json();
      onLogin(data.userId);
    } catch (err) {
      setError('Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      {loading && <Loading />}
      <div className="login-box">
        <img src={logo} className="logo" alt="logo" />
        <h2>Welcome back</h2>
        <p>Glad to see you again 👋</p>
        <p>Login to your account below</p>
        <form onSubmit={handleLogin}>
          <input
            type="text"
            placeholder="enter username..."
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <div className="password-container">
            <input
              type={showPassword ? 'text' : 'password'}
              placeholder="enter password..."
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              maxLength={20}
              required
            />
            <i
              className={`fas ${showPassword ? 'fa-eye-slash' : 'fa-eye'}`}
              onClick={() => setShowPassword(!showPassword)}
            ></i>
          </div>
          <div className="char-counter">{password.length}/20</div>
          <button type="submit">Login</button>
        </form>
        <p>
          <Link to="/forgot-password">Forgot Password?</Link>
        </p>
        <p>
          Don't have an account?{' '}
          <Link to="/register">Sign up for Free</Link>
        </p>
        {error && <p className="error">{error}</p>}
      </div>
    </div>
  );
};

export default Login;