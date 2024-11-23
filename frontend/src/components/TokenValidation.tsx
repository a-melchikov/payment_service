import React, { useEffect, useState } from 'react';
import { validateToken } from '../utils/validateToken';

const TokenValidation = () => {
  const [loading, setLoading] = useState(true);
  const [isValid, setIsValid] = useState(true);
  const [error, setError] = useState('');
  const [attempts, setAttempts] = useState(0);

  useEffect(() => {
    const maxAttempts = 10;
    const interval = 100;

    const checkSessionData = setInterval(() => {
      const token = sessionStorage.getItem('token');
      const userId = sessionStorage.getItem('userId');

      if (token && userId) {
        clearInterval(checkSessionData);
        validateToken(token, userId)
          .then((valid) => {
            setIsValid(valid);
            if (!valid) {
              setError('Токен недействителен. Пожалуйста, войдите снова.');
            }
            setLoading(false);
          })
          .catch(() => {
            setError('Ошибка при проверке токена. Попробуйте позже.');
            setLoading(false);
          });
      } else {
        setAttempts((prev) => prev + 1);
        if (attempts >= maxAttempts - 1) {
          clearInterval(checkSessionData);
          setError('Отсутствуют данные для аутентификации.');
          setIsValid(false);
          setLoading(false);
        }
      }
    }, interval);

    return () => clearInterval(checkSessionData);
  }, [attempts]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen bg-gray-100">
        <div className="text-center">
          <p className="text-lg text-gray-500 font-medium">Загрузка...</p>
        </div>
      </div>
    );
  }

  if (!isValid) {
    return (
      <div className="flex justify-center items-center h-screen bg-gray-100">
        <div className="bg-white shadow-lg rounded-lg p-6 max-w-md text-center">
          <p className="text-red-500 font-semibold text-lg mb-4">{error}</p>
          <p className="text-gray-500">Попробуйте обновить страницу или войдите снова.</p>
        </div>
      </div>
    );
  }

  return null;
};

export default TokenValidation;
