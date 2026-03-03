import './AddDoctor.css';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import toast, { Toaster } from 'react-hot-toast';


function AddPatient() {
    const navigate = useNavigate();
    const [personalNumber, setPersonalNumber] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [panNumber, setPanNumber] = useState('');
    const [email, setEmail] = useState('');

    async function createPatient() {
        const personalData = {
            firstName,
            lastName,
            email,
            role: 'PATIENT',
        };

        const patientRequest = {
            personalNumber,
            personalData,
            panNumber,
        };

        const res = await fetch('http://localhost:8080/api/patients', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(patientRequest),
        });

        if (res.ok) {
            toast.success('Pacient uspesne pridany!');
            navigate('/patients');
        } else {
            const error = await res.json();
            toast.error(error.message);
        }
    }

    return (
        <div className="page">
            <div className="card">
                <Toaster />
                <div className="form-fields">
                    <div className="field-group">
                        <label className="field-label" htmlFor="personalNumber">Rodne cislo *</label>
                        <input type="text" id="personalNumber" placeholder="Zadajte rodne cislo" className="field-input" onChange={(e) => setPersonalNumber(e.target.value)} />
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="firstName">Meno *</label>
                        <input type="text" id="firstName" placeholder="Zadajte meno" className="field-input" onChange={(e) => setFirstName(e.target.value)} />
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="lastName">Priezvisko *</label>
                        <input type="text" id="lastName" placeholder="Zadajte priezvisko" className="field-input" onChange={(e) => setLastName(e.target.value)} />
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="email">Email *</label>
                        <input type="email" id="email" placeholder="Zadajte email" className="field-input" onChange={(e) => setEmail(e.target.value)} />
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="pan">PAN cislo *</label>
                        <input type="text" id="pan" placeholder="Zadajte PAN cislo" className="field-input" onChange={(e) => setPanNumber(e.target.value)} />
                    </div>
                </div>
                <div className="group-btns">
                    <button className="btn btn-secondary" onClick={() => navigate('/patients')}>Zrusit</button>
                    <button className="btn btn-primary" onClick={() => createPatient()}>Pridat Pacienta</button>
                </div>
            </div>
        </div>
    );
}

export default AddPatient;
