interface BlueButtonProps {
  value: string;
}

export default function BlueButton({ value }: BlueButtonProps) {
  return (
    <button
      type="submit"
      className="w-full h-12 sm:h-10 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 text-white font-medium rounded-lg transition-colors mt-2 text-base sm:text-sm shadow-sm"
    >
      {value}
    </button>
  );
}
