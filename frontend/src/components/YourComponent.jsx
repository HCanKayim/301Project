import React from 'react';
import ErrorComponent from './ErrorComponent';

const YourComponent = () => {
  const [error, setError] = useState(null);

  // ... your other code

  return (
    <div>
      {error && <ErrorComponent error={error} />}
      {/* rest of your component */}
    </div>
  );
};

export default YourComponent; 