import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import enTranslation from '../lang/en/translation.json';
import plTranslation from '../lang/pl/translation.json';
import esTranslation from '../lang/es/translation.json';

const resources = {
    en: { translation: enTranslation },
    pl: { translation: plTranslation },
    es: {translation: esTranslation}
};

i18n
    .use(initReactI18next)
    .init({
        resources,
        lng: navigator.language.startsWith('pl')
            ? 'pl'
            : navigator.language.startsWith('es')
                ? 'es' : 'en',
        fallbackLng: 'en',
        interpolation: {
            escapeValue: false
        }
    });

export default i18n;