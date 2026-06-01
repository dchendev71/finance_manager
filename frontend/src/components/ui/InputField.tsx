interface InputFieldProps {
  label: string;
  id: string;
  type: "email" | "password" | "text" | "number";
}

export default function InputField({ label, id, type }: InputFieldProps) {
  return (
    <section className="w-full flex flex-col">
      <label
        className="block text-sm font-medium text-slate-700 mb-1.5"
        htmlFor={id}
      >
        {label}
      </label>
      <input
        className="w-full h-12 sm:h-10 px-3 py-2 border border-slate-300 rounded-lg text-base sm:text-sm focus:outline-none focus:ring-2 focus:ring-blue-5-- transition-shadow"
        type={type}
        id={id}
        name={id}
        required
      />
    </section>
  );
}
