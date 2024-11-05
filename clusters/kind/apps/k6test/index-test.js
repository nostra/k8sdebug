import { sleep, check } from 'k6';
import http from 'k6/http';

export default () => {
    const res = http.get('http://k8sdebug.apps:8080');
    check(res, {
        'status is 200': () => res.status === 200,
    });
    sleep(1);
};