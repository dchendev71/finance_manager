// src/components/ui/FormSuccessBanner.tsx
interface FormSuccessBannerProps {
  message: string | null;
}

export default function FormSuccessBanner({ message }: FormSuccessBannerProps) {
  // Gracefully render nothing if there is no active success state
  if (!message || message.trim() === "") return null;

  return (
    <div
      role="status" /* 💡 Accessibility: status informs screen readers non-disruptively */
      className="w-full flex items-start gap-3 bg-emerald-50/80 backdrop-blur-sm border border-emerald-200 p-4 rounded-xl text-emerald-800 text-sm animate-fade-in mb-5"
    >
      {/* Dynamic Success Checkmark Icon Layer */}
      <svg
        className="h-5 w-5 text-emerald-500 shrink-0 mt-0.5"
        viewBox="0 0 20 20"
        fill="currentColor"
        aria-hidden="true"
      >
        <path
          fillRule="evenodd"
          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z"
          clipRule="evenodd"
        />
      </svg>

      <div className="flex-1">
        <h5 className="font-semibold tracking-tight text-emerald-900 mb-0.5">
          Changes Saved
        </h5>
        <p className="text-emerald-700 leading-relaxed text-xs">{message}</p>
      </div>
    </div>
  );
}
