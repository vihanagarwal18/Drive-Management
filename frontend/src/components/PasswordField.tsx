import React, { useState } from 'react';
import './PasswordField.css';

interface PasswordFieldProps {
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder?: string;
    maxLength?: number;
    id?: string;
    required?: boolean;
}

const PasswordField: React.FC<PasswordFieldProps> = ({ value, onChange, placeholder, maxLength = 20, id, required = false }) => {
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div className="password-field-container">
            <div className="password-wrapper">
                <input
                    type={showPassword ? 'text' : 'password'}
                    value={value}
                    onChange={onChange}
                    placeholder={placeholder}
                    maxLength={maxLength}
                    id={id}
                    required={required}
                />
                <span className="password-toggle-icon" onClick={() => setShowPassword(!showPassword)}>
                    {showPassword ? '🙈' : '👁️'}
                </span>
            </div>
            <div className="char-counter">
                {value.length}/{maxLength}
            </div>
        </div>
    );
};

export default PasswordField;