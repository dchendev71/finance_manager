import Button from "@/components/ui/Button";
import InputField from "@/components/ui/InputField";
import { updateUser, type UpdateUserPayload } from "@/components/user/api";
import { useAuth } from "@/components/auth/AuthContext";
import { useUser } from "@/components/user/UserContext";
import { useState } from "react";
import FormErrorBanner from "@/components/ui/FormErrorBanner";
import FormSuccessBanner from "@/components/ui/FormSuccessBanner";

export default function ChangeEmailForm() {
  const { request } = useAuth();
  const { setUser } = useUser();
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
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

    try {
      await updateUser(payload);
      setSuccessMessage("Currency update succesful!");
    } catch (e: any) {
      setError(e.message || "Network error - please try again");
    }
  }
  return (
    <>
      <FormSuccessBanner message={successMessage} />
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
