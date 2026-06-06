import React from "react";

// 1. Define the allowed color variants as a union type
type ButtonVariant = "blue" | "green" | "red";

interface ButtonProps {
  value: string;
  onClick?: (event: React.MouseEvent<HTMLButtonElement>) => void;
  type?: "button" | "submit" | "reset";
  variant?: ButtonVariant; // Made optional with a default value below
}

// 2. Create a static mapping object for the color tokens
const VARIANT_MAP: Record<ButtonVariant, string> = {
  blue: "bg-blue-600 hover:bg-blue-700 active:bg-blue-800 focus:ring-blue-500",
  green:
    "bg-emerald-600 hover:bg-emerald-700 active:bg-emerald-800 focus:ring-emerald-500",
  red: "bg-rose-600 hover:bg-rose-700 active:bg-rose-800 focus:ring-rose-500",
};

export default function Button({
  value,
  onClick,
  type = "submit",
  variant = "blue", // Default fallback variant
}: ButtonProps) {
  // 3. Extract the correct color classes based on the active variant
  const colorClasses = VARIANT_MAP[variant];

  return (
    <button
      type={type}
      onClick={onClick}
      // Dynamic color classes injected seamlessly alongside core layout styles
      className={`px-5 h-12 sm:h-10 text-white font-medium rounded-lg transition-colors mt-2 text-base sm:text-sm shadow-sm focus:outline-none focus:ring-2 ${colorClasses}`}
    >
      {value}
    </button>
  );
}
