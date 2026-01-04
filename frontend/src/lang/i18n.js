import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';

const resources = {
    en: {
        translation: {
            "error.login.invalid": "Invalid email or password",
            "auth.server.error": "Server error",
            "error.validation": "Validation error"
        }
    },
    pl: {
        translation: {
            "error.login.invalid": "Nieprawidłowe dane logowania",
            "auth.server.error": "Błąd serwera",
            "error.validation": "Błąd walidacji"
        }
    }
};

i18n
    .use(initReactI18next)
    .init({
        resources,
        lng: navigator.language.startsWith('pl') ? 'pl' : 'pl',
        fallbackLng: 'pl',
        interpolation: {
            escapeValue: false
        }
    });

export default i18n;