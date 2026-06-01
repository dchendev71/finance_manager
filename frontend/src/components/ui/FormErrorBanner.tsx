// src/components/ui/FormErrorBanner.tsx
interface FormErrorBannerProps {
  message: string | null;
}

export default function FormErrorBanner({ message }: FormErrorBannerProps) {
  // Gracefully render nothing if there is no active error state
  if (!message || message === "") return null;

  return (
    <div
      role="alert"
      className="w-full flex items-start gap-3 bg-red-50/80 backdrop-blur-sm border border-red-200 p-4 rounded-xl text-red-800 text-sm animate-fade-in mb-5"
    >
      {/* Dynamic Warning Icon Layer */}
      <svg
        className="h-5 w-5 text-red-500 shrink-0 mt-0.5"
        viewBox="0 0 20 20"
        fill="currentColor"
        aria-hidden="true"
      >
        <path
          fillRule="evenodd"
          d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z"
          clip-rule="evenodd"
        />
      </svg>

      <div className="flex-1">
        <h5 className="font-semibold tracking-tight text-red-900 mb-0.5">
          Submission Error
        </h5>
        <p className="text-red-700 leading-relaxed text-xs font-mono bg-white/50 p-1.5 rounded border border-red-100 mt-1">
          {message}
        </p>
      </div>
    </div>
  );
}
