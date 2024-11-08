import {defineConfig} from 'vitepress'

// https://vitepress.vuejs.org/config/app-configs
export default defineConfig({
    // extends: DefaultTheme,
    head: [
        ['link', {rel: 'icon', type: 'image/svg+xml', href: '/img/logo.svg'}],
        ['meta', {name: 'keywords', content: 'SessionAPI, Minecraft, game, containerization, server, minigames'}],
        ['meta', {name: 'author', content: 'Kamil Szelągiewicz'}],
        ['meta', {property: 'og:title', content: 'SessionAPI Documentation'}],
        ['meta', {property: 'og:description', content: 'Library for game containerization on Minecraft servers.'}],
        ['meta', {property: 'og:image', content: '/img/logo.svg'}]
    ],
    title: 'SessionAPI',
    description: 'The library facilitates game containerization on a Minecraft server, enabling the creation of isolated environments within the game with separate logic and state.',
    outDir: "../../docs",
    themeConfig: {
        logo: 'img/logo.svg',
        nav: [
            {text: 'Home', link: '/'},
            {text: 'GitHub', link: 'https://github.com/Szelagi/SessionAPI'},
        ],
        socialLinks: [
            {icon: 'discord', link: 'https://discord.gg/za2pYfGWRN'},
            {icon: 'linkedin', link: 'https://www.linkedin.com/in/kamil-szelagiewicz/'},
        ],
        sidebar: [
            {
                text: 'Podstawy',
                items: [
                    {text: 'Wprowadzenie', link: '/'},
                    {text: 'Instalacja', link: '/installation'},
                    {text: 'Pierwszy projekt', link: '/first-project'},
                    // {text: 'Kreator mapy', link: '/creator'},
                ]
            },
            {
                text: 'Dokumentacja',
                items: [
                    // {text: 'Kreator mapy', link: '/'},
                    // {text: 'Adnotacje przestrzenne', link: '/getting-started/faq'},
                    // {text: 'Sesja (Session)', link: '/getting-started/installation'},
                    // {text: 'Mapa (Board)', link: '/getting-started/quick-start'},
                    // {text: 'Kontroller (Controller)', link: '/getting-started/faq'},
                    // {text: 'Zagnieżdzenia', link: '/getting-started/faq'},
                    // {text: 'Listenery', link: '/getting-started/faq'},
                    // {text: 'Zadania i wątki', link: '/getting-started/faq'},

                ]
            },
            {
                text: 'Wbudowane komponenty',
                items: []
            },
            {
                text: 'Publikacje',
                items: [
                    {text: 'Bezpieczeństwo w grach', link: '/security'}
                ]
            },
        ],
        darkModeSwitch: true,
        search: true,
        localeLinks: {
            text: "Language",
            items: [
                {text: "Polski", link: '/'},
                {text: "English", link: '/en/'}
            ]
        }
    },
});
