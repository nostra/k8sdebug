import soakTest from './index-test.js';
import { Trend } from 'k6/metrics';

export const options = {
    stages: [
        { duration: '5m', target: 50 }, // traffic ramp-up from 1 to 100 users over 5 minutes.
        { duration: '60m', target: 50 },
        { duration: '5m', target: 0 }, // ramp-down to 0 users
    ],
};

const customTrend = new Trend('soak_test');

export default function () {
    soakTest();
}