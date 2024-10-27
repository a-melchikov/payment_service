import { motion } from "framer-motion";
import useScreenWidth from "../hooks/useScreenWidth";

interface IMethodCardProps {
	imageSource: string;
	text: string;
}

function MethodCard(props: IMethodCardProps) {
	const buttonVariants = {
		click: { scale: 1, transition: { duration: 0.1 } },
		tap: { scale: 0.95, transition: { duration: 0.1 } },
	};

	const screenWidth = useScreenWidth();

	return (
		<motion.div
			whileTap={screenWidth > 1024 ? "click" : "tap"}
			whileHover={{ scale: 1.04, transition: { duration: 0.3 } }}
			variants={buttonVariants}
			className="flex items-center px-11 max-w-[450px] w-full tabletS:h-[80px] mobileS:h-[60px] shadow-methodCardShadow laptop:rounded-[20px] tabletS:rounded-[16px] mobileM:rounded-[14px] mobileS:rounded-[12px] bg-primary cursor-pointer"
		>
			<img
				src={props.imageSource}
				alt=""
				className="w-1/5 max-[425px]:w-1/3 grow-0"
			/>
			<div className="flex grow justify-center items-center mx-5">
				<p className="tabletS:text-[16px] mobileS:text-[12px] text-center">
					{props.text}
				</p>
			</div>
		</motion.div>
	);
}

export default MethodCard;
