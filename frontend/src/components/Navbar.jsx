import './Navbar.css';
import { IoPersonCircle, IoPeople, IoCalendarOutline, IoPulseSharp } from "react-icons/io5";
import { MdOutlineArticle, MdAdd  } from "react-icons/md";
import { NavLink } from 'react-router-dom';

const menus = {
    admin: [
        { to: '/doctors', icon: <IoPeople size={24} />, label: 'Prehľad lekárov' },
        { to: '/patients', icon: <IoPeople size={24} />, label: 'Prehľad pacientov' },
    ],
    doctor: [
        { to: '/doctor-preview-of-patients', icon: <IoPeople size={24} />, label: 'Prehľad pacientov' },
        { to: '/articles', icon: <MdOutlineArticle size={24} />, label: 'Články'},
    ],
    patient: [
        { to: '/add-measurement', icon: <MdAdd  size={24} />, label: 'Pridať meranie' },
        { to: '/measurements', icon: <IoPulseSharp size={24} />, label: 'Prehľad meraní' },
        { to: '/measurement-plan', icon: <IoCalendarOutline size={24} />, label: 'Plán monitorovania'},
        { to: '/articles', icon: <MdOutlineArticle size={24} />, label: 'Články'},
    ],
};

function Navbar({variant, profileName, profileRole}) {
    const items = menus[variant];

    return (
        <nav className="navbar">
            <div className='profile'>
                <IoPersonCircle size={80}/>
                 <div>
                <h2>{profileName}</h2>
                <p>{profileRole}</p>
                </div>
            </div>
            <div className="nav-menu">
                {items.map((item) => (
                    <NavLink key={item.to} to={item.to} className={({isActive}) => `nav-item ${isActive ? 'active' : ''}`} >
                        {item.icon}
                        {item.label}
                    </NavLink>
                ))}
            </div>
            <NavLink to="/login" className="logout">Odhlásiť sa</NavLink>
        </nav>
    );
}

export default Navbar;