import Button from "@/components/ui/Button";
import InputField from "@/components/ui/InputField";
import { changeEmail } from "@/components/user/api";
import { useAuth } from "@/components/auth/AuthContext";
import { useUser } from "@/components/user/UserContext";
import { useState } from "react";

export default function ChangeEmailForm() {
  const { request } = useAuth();
  const { setUser } = useUser();
  const [error, setError] = useState<string | null>(null);
  async function handleAction(formData: FormData) {
    // TODO: Check if OK
    await changeEmail(
      formData,
      {
        requestFn: request,
        errorFn: setError,
      },
      setUser,
    );
  }
  return (
    <>
      <form className="flex flex-col gap-5" action={handleAction}>
        <InputField
          id="currentPassword"
          type="password"
          label="Current Password"
        />
        <InputField id="newEmail" type="email" label="New Email" />
        <Button value="Change Email" variant="blue" type="submit" />
      </form>
    </>
  );
}
