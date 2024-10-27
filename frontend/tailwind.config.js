/** @type {import('tailwindcss').Config} */
export default {
	content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
	theme: {
		extend: {
			colors: {
				primary: "#F5F5F5",
				backBtn: "#E3E3E3",
				bankCardFront: "#4BA8E3",
				bankCardBack: "#1886CE",
				primaryBtn: "#52ABE3",
				inputField: "#EBEBEB",
				invalid: "#FF6347",
				valid: "#32CD32",
			},
			boxShadow: {
				methodCardShadow: "0 3px 10px -1px rgba(0, 0, 0, 0.35)",
				bankCardShadow: "0 3px 12px -1px rgba(0, 0, 0, 0.25)",
				btnShadow: "0 3px 10px -1px rgba(0, 0, 0, 0.25)",
			},
			screens: {
				mobileS: "320px",
				mobileM: "425px",
				tabletS: "567px",
				tabletM: "768px",
				laptop: "1024px",
				desktop: "1280px",
			},
			aspectRatio: {
				"741/402": "741 / 402",
				"580/360": "580 / 360",
			},
			fontSize: {
				adaptive: "calc(12px + 12 * (100vw / 1200))",
			},
		},
	},
	plugins: [],
};
