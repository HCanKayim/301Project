import React from 'react';

const ErrorComponent = ({ error }) => {
  // If error is an object with these properties
  if (error && typeof error === 'object') {
    return (
      <div className="error-message">
        <h3>Error {error.status}</h3>
        <p>{error.error}</p>
        <p>Time: {error.timestamp}</p>
        <p>Path: {error.path}</p>
      </div>
    );
  }

  // If error is just a string
  return (
    <div className="error-message">
      <p>{String(error)}</p>
    </div>
  );
};

export default ErrorComponent; 