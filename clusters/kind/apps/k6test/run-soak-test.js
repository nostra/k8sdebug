import soakTest from './index-test.js';
import { Trend } from 'k6/metrics';

export const options = {
    vus: 2,
    stages: [
        { duration: '5m', target: 100 }, // traffic ramp-up from 1 to 100 users over 5 minutes.
        { duration: '30', target: 100 },
        { duration: '5m', target: 0 }, // ramp-down to 0 users
    ],
};

const customTrend = new Trend('soak_test');

export default function () {
    soakTest();
}