import soakTest from './soak-index-test.js';

export const options = {
    vus: 5,
    duration: '10s',
};

export default function () {
    soakTest();
}