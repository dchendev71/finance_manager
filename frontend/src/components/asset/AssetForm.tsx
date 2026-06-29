import { useAuth } from "@/components/auth/AuthContext";
import InputField from "@/components/ui/InputField";
import { handleAssetForm, type AssetRowData } from "./api";
import { type Dispatch, type SetStateAction } from "react";
import Button from "@/components/ui/Button";
import { useBalance } from "@/components/balance/BalanceContext";
import AssetDropDown from "@/components/ui/AssetDropDown";

export type AssetMethod = "CREATE" | "BUY" | "SELL";
export type FormProps = {
  formTitle: string;
  cancelForm: (event: React.MouseEvent<HTMLButtonElement>) => void;
  submitValue: string;
  defaultValue?: string;
  disabled?: boolean;
  assetPrice?: number;
};
export interface AssetFormProps {
  portfolioName: string;
  assetName?: string;
  assetMethod: AssetMethod;
  stateFn: Dispatch<SetStateAction<AssetRowData[]>>;
  setError: Dispatch<SetStateAction<string | null>>;
  formProps: FormProps;
}

export default function AssetForm({
  formProps,
  portfolioName,
  assetMethod,
  stateFn,
  setError,
}: AssetFormProps) {
  const { request } = useAuth();
  const { refreshBalance } = useBalance();

  async function handleForm(formData: FormData) {
    // Reset error
    setError(null);
    await handleAssetForm(formData, assetMethod, portfolioName, stateFn, {
      requestFn: request,
      errorFn: setError,
    });
    // We refresh the balance, this is the single point entry where money flow
    refreshBalance();
    // Form finished, we can hide the form
    formProps.cancelForm({} as React.MouseEvent<HTMLButtonElement>);
  }

  const assetInputField = (
    <>
      <InputField
        label="Asset Name"
        id="assetName"
        type="text"
        value={formProps.defaultValue}
      />
      <InputField
        id="unitPrice"
        label="Unit Price"
        type="number"
        value={formProps.assetPrice?.toString()}
        readOnly={true}
      />
    </>
  );

  const assetDropDown = <AssetDropDown />;
  return (
    <>
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
        <div className="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl animate-in fade-in zoom-in-95 duration-200">
          <h2 className="text-xl font-bold text-slate-900 mb-4">
            {formProps.formTitle}
          </h2>

          <form action={handleForm}>
            {assetMethod === "CREATE" ? assetDropDown : assetInputField}
            <InputField
              label="Quantity"
              id="quantity"
              type="number"
              step={0.01}
              min={0}
            />
            <section className="flex flex-row gap-2">
              <Button
                value="Cancel"
                variant="blue"
                onClick={formProps.cancelForm}
              />
              <Button
                type="submit"
                value={formProps.submitValue}
                variant="blue"
              />
            </section>
          </form>
        </div>
      </div>
    </>
  );
}
