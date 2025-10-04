import React from 'react';
import './About.css';

const About: React.FC = () => {
  return (
    <div className="about-container">
      <h2>About the Developer</h2>
      <p>Connect with me on social media:</p>
      <div className="social-links">
        <a href="https://github.com/vihanagarwal18" target="_blank" rel="noopener noreferrer">
          <i className="fab fa-github"></i> GitHub
        </a>
        <a href="https://www.linkedin.com/in/vihan-agarwal-058513237/" target="_blank" rel="noopener noreferrer">
          <i className="fab fa-linkedin"></i> LinkedIn
        </a>
        <a href="https://www.instagram.com/_vihanagarwal_/" target="_blank" rel="noopener noreferrer">
          <i className="fab fa-instagram"></i> Instagram
        </a>
      </div>
    </div>
  );
};

export default About;