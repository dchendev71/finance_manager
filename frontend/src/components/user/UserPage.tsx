import Button from "@/components/ui/Button";
import { useUser } from "./UserContext";
import { formatToUTCLibrary } from "@/services/api";

export default function UserPage() {
  const { user } = useUser();
  return (
    <div className="min-h [calc(100vh-4rem)] bg-slate-100 text-slate-950 p-8">
      <div className="max-w-5xl mx-auto">
        <div className="mb-8 border-b border-slate-800 pb-4">
          <h1 className="text-2xl font-bold ">Account Settings</h1>
          <p className="text-sm text-slate-400 mt-1">
            Manage your profile credentials and authentication security details.
          </p>
        </div>

        <div className="grid grid-cols-3 gap-8 item-start">
          <div className="bg-slate-100 border border-slate-800 rounded-xl p-4 flex flex-col space-y-1">
            <Button value="Change Email" variant="blue" />
            <Button value="Change Password" variant="blue" />
            <Button value="Go to Dashboard" variant="blue" />
            <Button value="Portfolios" variant="blue" />
            <Button value="Performance" variant="blue" />
            <Button value="Change Currency" variant="blue" />
          </div>
          <div className="col-span-2 bg-slate-100 border border-slate-800 rounded-xl p-6 min-h-[300px]">
            <dl className="flex flex-col gap-4">
              <div>
                <dt className="text-xs text-slate-500">Email</dt>
                <dd className="text-sm font-semibold">{user?.email}</dd>
              </div>

              <div>
                <dt className="text-xs text-slate-500">Currency</dt>
                <dd className="text-sm font-semibold">
                  <dl className="flex flex-row gap-4">
                    <div>
                      <dt className="text-xs text-slate-400">Name</dt>
                      <dd className="text-sm font-semibold">
                        {user?.currency.name}
                      </dd>
                    </div>
                    <div>
                      <dt className="text-xs text-slate-400">Code</dt>
                      <dd className="text-sm font-semibold">
                        {user?.currency.code}
                      </dd>
                    </div>
                    <div>
                      <dt className="text-xs text-slate-400">Symbol</dt>
                      <dd className="text-sm font-semibold">
                        {user?.currency.symbol}
                      </dd>
                    </div>
                  </dl>
                </dd>
              </div>
              <div>
                <dt className="text-xs text-slate-500">Created at</dt>
                <dd className="text-sm font-semibold">
                  {formatToUTCLibrary(user?.createdAt)}
                </dd>
              </div>
            </dl>
          </div>
        </div>
      </div>
    </div>
  );
}
