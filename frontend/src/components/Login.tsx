import React, { useState } from 'react';
import './Login.css';
import logo from '../logo.svg';

interface LoginProps {
  onLogin: (userId: string) => void;
}

const Login: React.FC<LoginProps> = ({ onLogin }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [address, setAddress] = useState('');
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
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
    }
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    try {
      const response = await fetch('/internal/v1/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          password,
          name,
          email,
          phoneNumber,
          address,
        }),
      });

      if (!response.ok) {
        throw new Error('Registration failed');
      }

      const data = await response.json();
      onLogin(data.userId);
    } catch (err) {
      setError('Registration failed. Please try again.');
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <img src={logo} className="logo" alt="logo" />
        {isLogin ? (
          <>
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
              Don't have an account?{' '}
              <span className="link" onClick={() => setIsLogin(false)}>
                Sign up for Free
              </span>
            </p>
          </>
        ) : (
          <>
            <h2>Create an Account</h2>
            <p>Join us and start chatting!</p>
            <form onSubmit={handleRegister}>
              <input
                type="text"
                placeholder="Enter Your Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
              <input
                type="email"
                placeholder="Enter Your Email ID"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
              <input
                type="text"
                placeholder="Enter Your Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
              <input
                type="text"
                placeholder="Enter Your Phone Number"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
              />
              <input
                type="text"
                placeholder="Enter Your Address"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
              />
              <div className="password-container">
                <input
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Enter Password"
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
              <div className="password-container">
                <input
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Confirm Password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  maxLength={20}
                  required
                />
                <i
                  className={`fas ${showPassword ? 'fa-eye-slash' : 'fa-eye'}`}
                  onClick={() => setShowPassword(!showPassword)}
                ></i>
              </div>
              <div className="char-counter">{confirmPassword.length}/20</div>
              <button type="submit">Submit</button>
            </form>
            <p>
              Already have an account?{' '}
              <span className="link" onClick={() => setIsLogin(true)}>
                Login
              </span>
            </p>
          </>
        )}
        {error && <p className="error">{error}</p>}
      </div>
    </div>
  );
};

export default Login;