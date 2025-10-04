import React from 'react';

interface SettingsProps {
  isDarkMode: boolean;
  setIsDarkMode: (isDarkMode: boolean) => void;
}

const Settings: React.FC<SettingsProps> = ({ isDarkMode, setIsDarkMode }) => {
  return (
    <div className="settings-section">
      <h2>Settings</h2>
      <div className="setting-option">
        <label htmlFor="theme-toggle">Dark Mode</label>
        <div className="toggle-switch">
          <input
            type="checkbox"
            id="theme-toggle"
            checked={isDarkMode}
            onChange={() => setIsDarkMode(!isDarkMode)}
          />
          <label className="slider" htmlFor="theme-toggle"></label>
        </div>
      </div>
    </div>
  );
};

export default Settings;