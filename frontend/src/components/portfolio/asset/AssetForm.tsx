import { useAuth } from "@/components/auth/AuthContext";
import BlueButton from "@/components/ui/BlueButton";
import InputField from "@/components/ui/InputField";
import { handleAssetForm } from "./api";
import { useState } from "react";

// TODO: Make it so that Asset Name can or can not be changed
// Make it so that BlueButton value is dependent on the caller
//
export interface AssetFormProps {
  portfolioName: string;
}

export default function AssetForm({ portfolioName }: AssetFormProps) {
  const [error, setError] = useState<string | null>(null);
  const { request } = useAuth();

  async function handleForm(formData: FormData) {
    await handleAssetForm(formData, "CREATE", portfolioName, {
      requestFn: request,
      errorFn: setError,
    });
  }
  return (
    <>
      <form action={handleForm}>
        <InputField label="Asset Name" id="assetName" type="text" />
        <InputField label="Quantity" id="quantity" type="number" />
        <InputField label="Unit Price" id="unitPrice" type="number" />
        <BlueButton value="Add asset" />
      </form>
    </>
  );
}
