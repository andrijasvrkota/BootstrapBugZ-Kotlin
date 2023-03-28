import { HttpRequest, makeRequest } from '$lib/apis/api';
import { redirect } from '@sveltejs/kit';
import type { RequestHandler } from './$types';

export const GET = (async ({ locals, url }) => {
  if (locals.user) throw redirect(302, '/');

  const token = url.searchParams.get('token');
  const response = await makeRequest({
    method: HttpRequest.PUT,
    path: '/auth/confirm-registration',
    body: JSON.stringify({ token }),
  });

  if ('error' in response) return new Response(String(response.error));

  throw redirect(303, '/auth/sign-in');
}) satisfies RequestHandler;