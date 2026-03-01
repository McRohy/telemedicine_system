import './PasswordPage.css';
import {useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {BsHeartPulse} from "react-icons/bs";
import toast, { Toaster } from 'react-hot-toast';


function PasswordPage() {
  const navigate = useNavigate();  
  const {token} = useParams(); 
  const [password, setPassword] = useState('');

  async function sendPassword() {
    const res = await fetch('http://localhost:8080/api/set-password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ token, password }),
    });

    if (res.ok) {
      navigate('/login');
    } else {
      const error = await res.json();
      toast.error(error.message);
    }
  }

    return (
       <div className="page">
         <Toaster />
        <div className="card">
            <div>
                <BsHeartPulse className='logo'/>   
                <h1>TELEMEDICINE</h1>
                <p> Vitajte, pre používanie telemedicínskeho systému si prosím nastavte svoje heslo, ktoré budete používať na prihlásenie. </p>
            </div>
            <div className='input-part'>
                <input type="password" placeholder="Zadajte heslo" className="input" required onChange={(e) => setPassword(e.target.value)} />
                <button className="btn btn-primary" onClick={() => sendPassword()}>
                    Nastaviť heslo
                </button>
            </div> 
        </div>
    </div>
    );
    }

export default PasswordPage;