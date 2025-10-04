import React, { useState } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import PasswordField from './PasswordField';
import Loading from './Loading';
import './Login.css';
import logo from '../logo.svg';

const ResetPassword = () => {
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setMessage('');
        setError('');
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        const token = new URLSearchParams(location.search).get('token');
        setLoading(true);
        try {
            const response = await fetch('/internal/v1/auth/reset-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ token, password }),
            });

            if (response.ok) {
                setMessage('Password has been reset successfully. You can now login with your new password.');
                setTimeout(() => {
                    navigate('/login');
                }, 3000);
            } else {
                const errorText = await response.text();
                setError(`Error: ${errorText}`);
            }
        } catch (error) {
            setError('An error occurred. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-container">
            {loading && <Loading />}
            <div className="login-box">
                <img src={logo} className="logo" alt="logo" />
                <h2>Reset Your Password</h2>
                <p>Enter your new password below.</p>
                <form onSubmit={handleSubmit}>
                    <PasswordField
                        id="password"
                        placeholder="Enter new password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <PasswordField
                        id="confirm-password"
                        placeholder="Confirm new password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                    <button type="submit">Submit</button>
                </form>
                {message && <p className="success">{message}</p>}
                {error && <p className="error">{error}</p>}
                <p>
                    Remember your password?{' '}
                    <Link to="/login">Login</Link>
                </p>
            </div>
        </div>
    );
};

export default ResetPassword;