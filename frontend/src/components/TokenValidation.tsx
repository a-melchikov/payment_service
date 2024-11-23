import { ReactNode, useEffect, useState } from "react";
import { validateToken } from "../utils/validateToken";

interface IPaymentData {
	user_id: string;
	total_price: string;
	payment_token: string;
}

interface ITokenValidationProps {
	children: ReactNode;
	paymentData: IPaymentData | undefined;
}

const TokenValidation = ({ children, paymentData }: ITokenValidationProps) => {
	const [isValid, setIsValid] = useState<boolean>(true);
	const [error, setError] = useState<string>("");
	const [attempts, setAttempts] = useState<number>(0);

	useEffect(() => {
		const maxAttempts = 10;
		const interval = 100;

		const checkSessionData = setInterval(() => {
			if (!paymentData) return;

			const token = paymentData?.payment_token;
			const userId = paymentData?.user_id;

			if (token && userId) {
				clearInterval(checkSessionData);
				validateToken(token, userId)
					.then((valid) => {
						setIsValid(valid);
						if (!valid) {
							setError("Токен недействителен. Пожалуйста, войдите снова.");
						}
					})
					.catch(() => {
						setError("Ошибка при проверке токена. Попробуйте позже.");
					});
			} else {
				setAttempts((prev) => prev + 1);
				if (attempts >= maxAttempts - 1) {
					clearInterval(checkSessionData);
					setError("Отсутствуют данные для аутентификации.");
					setIsValid(false);
				}
			}
		}, interval);

		return () => clearInterval(checkSessionData);
	}, [attempts, paymentData]);

	if (!isValid) {
		return (
			<div className="flex self-center justify-center items-center tabletS:mx-10 mobileS:mx-5 px-5 pt-8 max-w-[580px] w-full tabletS:h-[760px] mobileS:h-[600px] tabletM:rounded-[20px] tabletS:rounded-[16px] mobileM:rounded-[12px] mobileS:rounded-[8px] bg-primary">
				<div className="bg-white shadow-lg rounded-lg p-6 max-w-md text-center">
					<p className="text-red-500 font-semibold text-lg mb-4">{error}</p>
					<p className="text-gray-500">
						Попробуйте обновить страницу или войдите снова.
					</p>
				</div>
			</div>
		);
	}

	return <>{children}</>;
};

export default TokenValidation;
