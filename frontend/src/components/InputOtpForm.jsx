import {z} from "zod"
import { useForm } from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import { confirmSignUp } from 'aws-amplify/auth';
import {useNavigate} from "react-router";

export default function InputOtpForm( { email }) {

    const schema = z.object({
        otp: z
            .string()
            .min(6, "Verification code must be exactly 6 digits")
            .regex(/^\d+$/, "Verification code can only contain numbers")
    })

    const { register,
        handleSubmit,
        formState: {errors, isSubmitting},
        setError
    } = useForm({
        resolver: zodResolver(schema)
    });

    const navigate = useNavigate()

    const onVerifyOTP = async (data) => {
         try {
             const {isSignUpComplete } = await confirmSignUp({
                 username: email,
                 confirmationCode: data.otp
             });

             if (isSignUpComplete) {
                 navigate("/login", { replace: true})
             }


         } catch (error) {

             setError("otp", {
                 type:"server",
                 message: error.message
             })

         }
    }

    return (
        <form
            onSubmit={handleSubmit(onVerifyOTP)}
            className="w-full max-w-md flex flex-col gap-6 p-8 bg-black rounded-xl shadow-xl shadow-emerald-500/20 mx-auto"
        >

            <div className="flex flex-col gap-2 text-center mb-2">
                <h2 className="text-xl font-semibold text-white">Check your email</h2>
                <p className="text-sm text-neutral-400">
                    We sent a 6-digit code to <span className="text-emerald-400 font-medium">{email}</span>
                </p>
            </div>


            <div className="flex flex-col gap-1">
                <input
                    {...register("otp")}
                    type="text"
                    maxLength={6}
                    placeholder="000000"
                    // Added tracking-widest and text-center specifically to make the 6 digits look like a real OTP input
                    className="w-full px-4 py-3 bg-neutral-900 text-white text-center tracking-[0.5em] text-xl rounded-md border border-neutral-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 placeholder:text-neutral-700 transition-all"
                />
                {errors.otp && (
                    <div className="text-red-500 text-sm mt-1 text-center">{errors.otp.message}</div>
                )}
            </div>


            <button
                type="submit"
                disabled={isSubmitting}
                className="w-full px-4 py-3 mt-2 bg-emerald-600 text-white rounded-md font-semibold hover:bg-emerald-500 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
                {isSubmitting ? "Verifying..." : "Verify Code"}
            </button>
        </form>
    );

}