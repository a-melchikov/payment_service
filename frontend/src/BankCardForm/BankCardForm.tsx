import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { useLocation, useNavigate } from "react-router-dom";
import useScreenWidth from "../hooks/useScreenWidth";
import isPastDate from "../utils/isPastDate";

import { FaAngleLeft } from "react-icons/fa6";

interface IBankCardForm {
	cardNumber: string;
	date: string;
	cvc: string;
}

interface IPaymentData {
	user_id: string;
	total_price: string;
	payment_token: string;
}

function BankCardForm() {
	const location = useLocation();
	const navigate = useNavigate();
	const { containerWidth } = location.state;
	const [paymentData, setPaymentData] = useState<IPaymentData>();
	const [cardNumber, setCardNumber] = useState<string>("");
	const [date, setDate] = useState<string>("");
	const [cvc, setCvc] = useState<string>("");
	const [shouldSave, setShouldSave] = useState<boolean>(false);

	const screenWidth = useScreenWidth();
	const shouldAnimate = screenWidth > 660;
	const currentDate = new Date();

	useEffect(() => {
		const storedPaymentData = sessionStorage.getItem("paymentData");
		if (storedPaymentData) {
			setPaymentData(JSON.parse(storedPaymentData));
		}
	}, []);

	const {
		register,
		handleSubmit,
		setValue,
		trigger,
		formState: { errors },
	} = useForm<IBankCardForm>({
		mode: "onSubmit",
	});

	const formatCardNumber = (value: string) => {
		const cleaned = value.replace(/\s+|\D+/g, "").slice(0, 16);
		const formatted = cleaned.replace(/(\d{4})(?=\d)/g, "$1 ");
		return formatted;
	};

	const formatDate = (value: string) => {
		const numericValue = value.replace(/\D/g, "");
		const formattedValue = numericValue.slice(0, 4);

		if (formattedValue.length === 1 && Number(formattedValue) > 1) {
			return "0" + formattedValue;
		}
		if (formattedValue.length > 2) {
			return formattedValue.slice(0, 2) + "/" + formattedValue.slice(2);
		} else {
			return formattedValue;
		}
	};

	const formatCvc = (value: string) => {
		const formattedValue = value.replace(/\D/g, "");
		return formattedValue;
	};

	const handleCardNumberChange = async (
		e: React.ChangeEvent<HTMLInputElement>
	) => {
		const value = e.target.value;
		const formattedValue = formatCardNumber(value);
		setCardNumber(formattedValue);
		setValue("cardNumber", formattedValue);
		await trigger("cardNumber");
	};

	const handleDateChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value;
		const formattedValue = formatDate(value);
		setDate(formattedValue);
		setValue("date", formattedValue);
		await trigger("date");
	};

	const handleCvcChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value;
		const formattedValue = formatCvc(value);
		setCvc(formattedValue);
		setValue("cvc", formattedValue);
		await trigger("cvc");
	};

	const onSubmit: SubmitHandler<IBankCardForm> = async () => {
		try {
			const month = parseInt(date.slice(0, 2), 10);
			const year = parseInt(date.slice(3, 5), 10);

			const newMonth = month === 12 ? 1 : month + 1;
			const newYear = month === 12 ? year + 1 : year;

			const formattedExpiryDate = `20${newYear
				.toString()
				.padStart(2, "0")}-${newMonth.toString().padStart(2, "0")}-01`;
			const userId = paymentData?.user_id;
			const response = await fetch(
				`http://127.0.0.1:8080/api/v1/payments/bankcard?shouldSave=${shouldSave}&userId=${userId}`,
				{
					method: "POST",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${paymentData?.payment_token}`,
					},
					body: JSON.stringify({
						userId: paymentData?.user_id,
						cardNumber: cardNumber.replace(/\s/g, ""),
						cvv: cvc,
						paymentSum: paymentData?.total_price,
						expiryDate: formattedExpiryDate,
					}),
				}
			);

			const result = await response.json();

			if (result.responseStatus.status === "Успех") {
				console.log("Оплата прошла успешно");
			} else {
				console.log(result.responseStatus.message);
			}

			sessionStorage.setItem("paymentStatus", JSON.stringify(result));
			navigate("/bankCard/status");
		} catch (error) {
			console.log("Error processing payment:", error);
		}
	};

	const handleGoBack = () => {
		navigate(-1);
	};

	return (
		<motion.div
			initial={{ width: containerWidth }}
			animate={{ width: "100%" }}
			transition={
				shouldAnimate ? { duration: 0.6, ease: "easeInOut" } : { duration: 0 }
			}
			className="flex self-center justify-center items-center tabletS:mx-10 mobileS:mx-5 max-w-[1200px] w-full tabletS:h-[760px] mobileS:h-[600px] tabletM:rounded-[20px] tabletS:rounded-[16px] mobileM:rounded-[12px] mobileS:rounded-[8px] bg-primary"
		>
			<motion.div
				initial={{ opacity: 0, y: 35 }}
				animate={{ opacity: 1, y: 0 }}
				transition={
					shouldAnimate
						? { duration: 0.5, delay: 0.35, ease: "easeInOut" }
						: { duration: 0.5, delay: 0, ease: "easeInOut" }
				}
				className="flex flex-col justify-center items-center w-full h-full"
			>
				<div
					className="flex mt-[2%] ml-[5%] self-start items-center gap-2 cursor-pointer"
					onClick={handleGoBack}
				>
					<div className="flex justify-center items-center tabletS:w-[30px] tabletS:h-[30px] mobileS:w-[24px] mobileS:h-[24px] bg-backBtn rounded-[5px]">
						<FaAngleLeft className="tabletS:text-[20px] mobileS:text-[16px]" />
					</div>
					<p className="tabletS:text-[16px] mobileS:text-[12px]">
						Вернуться назад
					</p>
				</div>
				<form
					onSubmit={handleSubmit(onSubmit)}
					autoComplete="off"
					className="flex flex-col justify-center items-center w-full h-full tabletM:gap-5 mobileS:gap-10"
				>
					<p className="tabletS:text-[32px] mobileM:text-[24px] mobileS:text-[20px] font-medium text-center">
						Введите данные карты
					</p>
					<div className="grid grid-cols-8 grid-rows-6 self-center tabletS:w-[70%] mobileS:w-[80%] aspect-[740/400]">
						<div
							style={{
								gridRow: "1 / 6",
								gridColumn: "1 / 7",
							}}
							className="flex flex-col justify-center w-full h-auto bg-bankCardFront tabletM:shadow-bankCardShadow mobileS:shadow-bankCardShadowMobile z-10 tabletM:gap-12 mobileM:gap-14 aspect-[580/360] laptop:rounded-[20px] tabletS:rounded-[16px] mobileM:rounded-[12px] mobileS:rounded-[8px]"
						>
							<div className="flex aspect-[422/222] self-center flex-col justify-center w-full max-w-[90%]">
								<label
									htmlFor="cardNumber"
									className="block w-full desktop:text-[18px] laptop:text-[16px] tabletM:[14px] tabletS:text-[12px] mobileM:text-[10px] mobileS:text-[8px]"
								>
									Номер карты
								</label>
								<input
									type="text"
									id="cardNumber"
									placeholder="0000 0000 0000 0000"
									{...register("cardNumber", {
										required: "Обязательное поле",
										pattern: {
											value: /^(?:\d{4}\s?){3}\d{4}$/,
											message: "Неверный формат номера карты",
										},
										onChange: handleCardNumberChange,
									})}
									value={cardNumber}
									className={`h-[15%] box-border desktop:text-[18px] laptop:text-[16px] tabletM:[14px] tabletS:text-[12px] mobileM:text-[10px] mobileS:text-[8px] laptop:pl-[6px] mobileS:pl-[3px] mobileM:py-2 mobileS:py-[6px] tabletS:border-2 mobileS:border-[1px] border-solid border-transparent transition-colors duration-200 outline-none bg-inputField laptop:rounded-[8px] tabletS:rounded-[5px] mobileS:rounded-[3px] ${
										errors.cardNumber
											? "focus:border-invalid border-invalid"
											: "focus:border-valid"
									}`}
								/>
								<label
									htmlFor="validThru"
									className="desktop:text-[18px] laptop:text-[16px] tabletM:[14px] tabletS:text-[12px] mobileM:text-[10px] mobileS:text-[8px] mt-[10%]"
								>
									Действует до
								</label>
								<input
									type="text"
									id="validThru"
									placeholder="ММ/ГГ"
									{...register("date", {
										required: "Обязательное поле",
										pattern: {
											value: /^(0[1-9]|1[0-2])\/([0-9]{2})$/,
											message: "Неверный формат даты",
										},
										onChange: handleDateChange,
										validate: (v) => !isPastDate(v, currentDate),
									})}
									value={date}
									className={`w-[20%] h-[15%] box-border desktop:text-[18px] laptop:text-[16px] tabletM:[14px] tabletS:text-[12px] mobileM:text-[10px] mobileS:text-[8px] laptop:pl-[6px] mobileM:pl-[3px] mobileS:pl-[1px] mobileM:py-2 mobileS:py-[6px] tabletS:border-[2px] mobileS:border-[1px] border-solid border-transparent transition-colors duration-200 outline-none bg-inputField laptop:rounded-[8px] tabletS:rounded-[5px] mobileS:rounded-[3px] ${
										errors.date
											? "focus:border-invalid border-invalid"
											: "focus:border-valid"
									}`}
								/>
							</div>
						</div>
						<div
							style={{ gridRow: "2 / 7", gridColumn: "3 / 9" }}
							className="flex flex-col w-full h-full bg-bankCardBack tabletM:shadow-bankCardShadow mobileS:shadow-bankCardShadowMobile z-0 aspect-[580/360] laptop:rounded-[20px] tabletS:rounded-[16px] mobileM:rounded-[12px] mobileS:rounded-[8px]"
						>
							<div className="w-full aspect-[580/70] mt-[8%] bg-[#1E1E1E]"></div>
							<input
								type="password"
								placeholder="CVC"
								{...register("cvc", {
									required: "CVC code is required",
									pattern: {
										value: /\d{3}/,
										message: "Неверный формат CVC-кода",
									},
									onChange: handleCvcChange,
								})}
								value={cvc}
								className={`w-[12%] h-[11%] self-end mr-[11%] mt-[5%] desktop:text-[18px] laptop:text-[16px] tabletM:[14px] tabletS:text-[12px] mobileM:text-[10px] mobileS:text-[8px] laptop:pl-[6px] mobileM:pl-[3px] mobileS:pl-[1px] mobileM:py-2 mobileS:py-[6px] tabletS:border-2 mobileS:border-[1px] border-solid border-transparent transition-colors duration-200 outline-none bg-inputField laptop:rounded-[8px] tabletS:rounded-[5px] mobileS:rounded-[3px] ${
									errors.cvc
										? "focus:border-invalid border-invalid"
										: "focus:border-valid"
								}`}
								maxLength={3}
							/>
						</div>
					</div>
					<div className="flex items-center gap-2">
						<input
							type="checkbox"
							id="saveCard"
							checked={shouldSave}
							onChange={(e) => setShouldSave(e.target.checked)}
							className="tabletS:w-4 tabletS:h-4 mobileS:w-3 mobileS:h-3"
						/>
						<label
							htmlFor="saveCard"
							className="tabletS:text-[16px] mobileS:text-[12px]"
						>
							Сохранить карту
						</label>
					</div>
					<motion.button
						whileTap={{ scale: 0.95, transition: { duration: 0.1 } }}
						className="tabletS:w-[200px] tabletS:h-[50px] mobileS:w-[160px] mobileS:h-[40px] bg-primaryBtn tabletM:shadow-btnShadow mobileS:shadow-btnShadowMobile rounded-[15px] tabletS:text-[24px] mobileS:text-[16px]"
					>
						Оплатить
					</motion.button>
				</form>
			</motion.div>
		</motion.div>
	);
}

export default BankCardForm;
