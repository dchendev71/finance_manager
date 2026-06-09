import Button from "@/components/ui/Button";
import InputField from "@/components/ui/InputField";
import { changePassword } from "../api";
import { useAuth } from "@/components/auth/AuthContext";
import { useUser } from "@/components/user/UserContext";
import { useState } from "react";

export default function ChangePasswordForm() {
  const { request } = useAuth();
  const { setUser } = useUser();
  const [error, setError] = useState<string | null>(null);
  async function handleAction(formData: FormData) {
    // TODO: Check if OK
    await changePassword(
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
        <InputField id="newPassword" type="password" label="New Password" />
        <InputField
          id="confirmPassword"
          type="password"
          label="Confirm New Password"
        />
        <Button value="Change Password" variant="blue" type="submit" />
      </form>
    </>
  );
}
