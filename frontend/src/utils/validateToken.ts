export const validateToken = async (token: string, userId: string): Promise<boolean> => {
    try {
      const response = await fetch('http://localhost:8080/api/v2/token-validation', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          token,
          userId,
        }),
      });
  
      if (!response.ok) {
        throw new Error('Token validation failed');
      }
      
      const isValid = await response.json();
      return isValid;
    } catch (error) {
      console.error('Error validating token:', error);
      return false;
    }
  };
  