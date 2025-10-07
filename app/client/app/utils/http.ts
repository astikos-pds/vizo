export interface HttpClient {
  get<T>(
    uri: string,
    params?: Record<string, any>,
    headers?: Record<string, any>
  ): Promise<T>;
  post<T>(uri: string, body: any): Promise<T>;
  put<T>(uri: string, body: any): Promise<T>;
  patch<T>(uri: string, body?: any): Promise<T>;
  delete(uri: string): void;
}

export class HttpClientImpl implements HttpClient {
  constructor(private readonly api: typeof $fetch) {}

  public async get<T>(
    uri: string,
    params?: Record<string, any>,
    headers?: Record<string, any>
  ): Promise<T> {
    return this.api<T>(uri, {
      method: "GET",
      params,
      headers,
    });
  }

  public async post<T>(uri: string, body: any): Promise<T> {
    return this.api<T>(uri, {
      method: "POST",
      body,
    });
  }

  public async put<T>(uri: string, body: any): Promise<T> {
    return this.api<T>(uri, {
      method: "PUT",
      body,
    });
  }

  public async patch<T>(uri: string, body: any): Promise<T> {
    return this.api<T>(uri, {
      method: "PATCH",
      body,
    });
  }

  public async delete(uri: string): Promise<void> {
    this.api(uri, {
      method: "DELETE",
    });
  }
}
