import {
  uploadImageUseCase,
  type UploadImageRequest,
  type UploadImageResponse,
} from "~/services/upload";

export const useImage = () => {
  const { loading, handle } = useApiHandler();

  async function uploadImage(request: UploadImageRequest): Promise<string> {
    const response = await handle<UploadImageResponse>(() =>
      uploadImageUseCase(request)
    );

    return response?.secure_url ?? "";
  }

  return {
    loading,
    uploadImage,
  };
};
