import {useState} from "react";
import {FaEye, FaEyeSlash} from "react-icons/fa";

const PasswordInput = ({value, handleChange, errors = {}}) => {
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div>
            <div style={{position: "relative", width: "100%"}}>
                <input
                    type={showPassword ? "text" : "password"}
                    name="password"
                    value={value || ""}
                    onChange={handleChange}
                    placeholder="Enter password"
                />
                <span onClick={() => setShowPassword(prev => !prev)} className="passwordIcon">
                    {showPassword ? <FaEyeSlash/> : <FaEye/>}
                </span>
            </div>
            {errors.password && <span className="error">{errors.password}</span>}
        </div>
    );
};

export default PasswordInput;