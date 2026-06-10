import Button from "@/components/ui/Button";
import InputField from "@/components/ui/InputField";
import { updateUser, type UpdateUserPayload } from "@/components/user/api";
import { useAuth } from "@/components/auth/AuthContext";
import { useUser } from "@/components/user/UserContext";
import { useState } from "react";
import FormErrorBanner from "@/components/ui/FormErrorBanner";

export default function ChangeEmailForm() {
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
        endpoint: "/users/change-email",
        method: "PATCH",
        requiredFields: ["currentPassword", "newEmail"],
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
        <InputField id="newEmail" type="email" label="New Email" />
        <Button value="Change Email" variant="blue" type="submit" />
      </form>
    </>
  );
}
