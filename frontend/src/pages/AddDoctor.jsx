import './AddDoctor.css';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import toast, { Toaster } from 'react-hot-toast';

const specializations = [
    'Cardiologist',
    'Oncologist',
];

function AddDoctor() {
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [panNumber, setPanNumber] = useState('');
    const [specialization, setSpecialization] = useState('');
    const [email, setEmail] = useState('');

    async function createDoctor() {
        const personalData = {
            firstName,
            lastName,
            email,
            role: 'DOCTOR',
        };

        const doctorRequest = {
            panNumber,
            personalData,
            specialization,
        };

        const res = await fetch('http://localhost:8080/api/doctors', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctorRequest),
        });

        if (res.ok) {
            navigate('/doctors');
        } else {
            const error = await res.json();
            toast.error(error.message);
        }
    }

    return (
        <div className="page">
            <Toaster />
            <div className="card">
                <div className="form-fields">
                    <div className="field-group">
                        <label className="field-label" htmlFor="firstName">Meno *</label>
                        <input type="text" id="firstName" placeholder="Zadajte meno" className="field-input" onChange={(e) => setFirstName(e.target.value)} />
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="lastName">Priezvisko *</label>
                        <input type="text" id="lastName" placeholder="Zadajte priezvisko" className="field-input" onChange={(e) => setLastName(e.target.value)} />
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="pan">PAN cislo *</label>
                        <input type="text" id="pan" placeholder="Zadajte PAN cislo" className="field-input" onChange={(e) => setPanNumber(e.target.value)} />
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="specialization">Odbornost *</label>
                        <select id="specialization" className="field-input" onChange={(e) => setSpecialization(e.target.value)}>
                            <option value="">Vyberte odbornost</option>
                            {specializations.map((spec) => (
                                <option key={spec} value={spec}>{spec}</option>
                            ))}
                        </select>
                    </div>
                    <div className="field-group">
                        <label className="field-label" htmlFor="email">Email *</label>
                        <input type="email" id="email" placeholder="Zadajte email" className="field-input" onChange={(e) => setEmail(e.target.value)} />
                    </div>
                </div>
                <div className="group-btns">
                    <button className="btn btn-secondary" onClick={() => navigate('/doctors')}>Zrusit</button>
                    <button className="btn btn-primary" onClick={() => createDoctor()}>Pridat Lekara</button>
                </div>
            </div>
        </div>
    );
}

export default AddDoctor;
