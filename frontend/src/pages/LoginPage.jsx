import './PasswordPage.css';
import {BsHeartPulse} from "react-icons/bs";


function LoginPage() {

    return (
       <div className="page">
        <div className="card">
            <div>
                <BsHeartPulse className='logo'/>   
                <h1>TELEMEDICINE</h1>
                <p> Vitajte, pre používanie telemedicínskeho systému sa prosím prihláste pomocou svojho hesla. </p>
            </div>
            <div className='input-part'>
                <input type="email" id="email" placeholder="Zadajte email" className="input" />
                <input type="password" id="password" placeholder="Zadajte heslo" className="input" />
                <button id="submit" className="btn btn-primary">
                    Prihlásiť sa
                </button>
            </div> 
        </div>
    </div>
    );
    }

export default LoginPage;