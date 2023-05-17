import type { JwtPayload } from '$lib/models/auth/jwt-payload';
import type { Cookies } from '@sveltejs/kit';
import jwt from 'jsonwebtoken';

export enum HttpRequest {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  DELETE = 'DELETE',
}

export function removeBearerPrefix(token: string): string {
  return token.replace('Bearer ', '');
}

export function setAccessTokenCookie(cookies: Cookies, accessToken: string): void {
  const { exp } = jwt.decode(removeBearerPrefix(accessToken)) as JwtPayload;
  cookies.set('accessToken', accessToken, {
    httpOnly: true,
    path: '/',
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'strict',
    expires: new Date(exp * 1000),
  });
}

export function setRefreshTokenCookie(cookies: Cookies, refreshToken: string): void {
  const { exp } = jwt.decode(removeBearerPrefix(refreshToken)) as JwtPayload;
  cookies.set('refreshToken', refreshToken, {
    httpOnly: true,
    path: '/',
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'strict',
    expires: new Date(exp * 1000),
  });
}

export function removeAuth(cookies: Cookies, locals: App.Locals): void {
  cookies.delete('accessToken', { path: '/' });
  cookies.delete('refreshToken', { path: '/' });
  locals.userId = null;
}