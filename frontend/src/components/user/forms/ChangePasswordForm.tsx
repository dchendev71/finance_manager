import Button from "@/components/ui/Button";
import InputField from "@/components/ui/InputField";
import { updateUser, type UpdateUserPayload } from "@/components/user/api";
import { useAuth } from "@/components/auth/AuthContext";
import { useUser } from "@/components/user/UserContext";
import { useState } from "react";
import FormErrorBanner from "@/components/ui/FormErrorBanner";

export default function ChangePasswordForm() {
  const { request } = useAuth();
  const { setUser } = useUser();
  const [error, setError] = useState<string | null>(null);
  async function handleAction(formData: FormData) {
    const payload: UpdateUserPayload = {
      formData: formData,
      callerFn: {
        requestFn: request,
        errorFn: setError,
      },
      setUser: setUser,
      config: {
        endpoint: "/users/change-password",
        method: "POST",
        requiredFields: ["currentPassword", "newPassword", "confirmPassword"],
      },
    };

    await updateUser(payload);
  }
  return (
    <>
      <FormErrorBanner message={error} />
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
